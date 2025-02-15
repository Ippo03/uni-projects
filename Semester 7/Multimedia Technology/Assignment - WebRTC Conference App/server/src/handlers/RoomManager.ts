import {Socket} from 'socket.io';

import {IRoom} from "../entities/Room";
import {IPeer} from "../entities/Peer";
import {WorkerManager} from "./WorkerManager";
import {audioLevelObserverConfig} from "../util/mediasoup/audioLevelObserverConfig";
import {mediasoupConfig} from "../util/mediasoup/mediasoupConfig";
import {logger} from "../util/logger";
import { ActiveSpeakerManager } from './ActiveSpeakerManager';

/**
 * Manages rooms and peers in a mediasoup-based real-time communication system.
 *
 * Responsible for creating, retrieving, and destroying rooms, as well as managing
 * peers and their resources (transports, producers, consumers) within those rooms.
 */
export class RoomManager {
    private rooms: Map<string, IRoom> = new Map();
    private activeSpeakerManager: ActiveSpeakerManager;

    constructor(private workerManager: WorkerManager) {
        this.activeSpeakerManager = new ActiveSpeakerManager();
    }

    /**
     * Creates a new room with the given ID and associated mediasoup router.
     *
     * @param roomId - The unique identifier for the room.
     * @returns A Promise that resolves to the created room (`IRoom`).
     * @throws Error if the room with the same ID already exists.
     */
    async createRoom(roomId: string): Promise<IRoom> {
        logger.debug(`Creating room with ID ${roomId}...`);

        if (this.rooms.has(roomId)) {
            throw new Error(`Cannot create room; room with ID ${roomId} already exists!`);
        }

        const worker = this.workerManager.getLeastLoadedWorker();
        const router = await worker.createRouter({
            mediaCodecs: mediasoupConfig.mediasoup.router.mediaCodecs
        });

        const room: IRoom = {
            id: roomId,
            router,
            worker,
            peers: new Map(),
            producerAudioConsumersMuted: new Set<string>(),
        };

        room.audioLevelObserver = await router.createAudioLevelObserver({
            maxEntries: audioLevelObserverConfig.maxEntries,
            threshold: audioLevelObserverConfig.threshold,
            interval: audioLevelObserverConfig.interval,
        });

        this.establishRoomListeners(room);
        this.rooms.set(roomId, room);

        this.workerManager.incrementWorkerLoad(worker);
        logger.info(`Room ${roomId} created on worker ${worker.pid}.`);

        return room;
    }

    /**
     * Destroys a room by its ID, cleaning up all associated resources.
     *
     * @param roomId - The unique identifier for the room to destroy.
     */
    async destroyRoom(roomId: string): Promise<void> {
        const room = this.rooms.get(roomId);
        if (!room) {
            logger.debug(`Room ${roomId} not found for destruction`);
            return;
        }

        logger.debug(`Destroying room ${roomId}...`);

        try {
            for (const peer of room.peers.values()) {
                await this.removePeer(roomId, peer.id);
            }

            if (room.audioLevelObserver) {
                room.audioLevelObserver.close();
                logger.debug(`AudioLevelObserver closed for room ${roomId}`);
            }

            room.router.close();
            logger.debug(`Router closed for room ${roomId}`);

            this.workerManager.decrementWorkerLoad(room.worker);
            logger.debug(`Worker load decremented for room ${roomId}`);

            this.rooms.delete(roomId);
            logger.info(`Room ${roomId} destroyed successfully`);
        } catch (error) {
            logger.error(`Error destroying room ${roomId}:`, error);
            throw error;
        }
    }

    /**
     * Adds a peer to a room with the given room ID and peer ID.
     *
     * @param roomId - The ID of the room to add the peer to.
     * @param peerId - The unique identifier for the peer.
     * @param socket - The Socket.IO connection associated with the peer.
     * @returns A Promise that resolves to the created peer (`IPeer`).
     * @throws Error if the room does not exist or if the peer already exists in the room.
     */
    async addPeer(roomId: string, peerId: string, socket: Socket): Promise<IPeer> {
        logger.debug(`Adding peer ${peerId} to room ${roomId}...`)
        const room = this.rooms.get(roomId);
        if (!room) {
            throw new Error(`Room ${roomId} not found!`);
        }

        if (room.peers.has(peerId)) {
            throw new Error(`Peer ${peerId} already exists in room ${roomId}!`);
        }

        const peer: IPeer = {
            id: peerId,
            socket,
            transports: new Map(),
            producers: new Map(),
            consumers: new Map()
        };

        room.peers.set(peerId, peer);
        logger.info(`Peer ${peerId} joined room ${roomId}.`);
        return peer;
    }

    /**
     * Removes a peer from a room, cleaning up all their associated resources.
     *
     * @param roomId - The ID of the room the peer belongs to.
     * @param peerId - The unique identifier for the peer to remove.
     */
    async removePeer(roomId: string, peerId: string): Promise<void> {
        logger.debug(`Peer ${peerId} leaving room ${roomId}...`);

        const room = this.rooms.get(roomId);
        if (!room) return;

        const peer = room.peers.get(peerId);
        if (!peer) return;

        peer.producers.forEach((producer) => producer.close());
        peer.consumers.forEach((consumer) => consumer.close());
        peer.transports.forEach((transport) => transport.close());

        room.peers.delete(peerId);
        logger.info(`Peer ${peerId} left room ${roomId}.`);

        if (room.peers.size === 0) {
            logger.info(`Last peer of room ${roomId} left; destroying room.`);
            await this.destroyRoom(roomId);
        }
    }

    /**
     * Finds and retrieves the room associated with the given peer ID.
     *
     * @param peerId - The unique identifier of the peer.
     * @returns A Promise that resolves to the room (`IRoom`) or `undefined` if no room was found.
     */
    async getPeerRoom(peerId: string): Promise<IRoom | undefined> {
        for (const room of this.rooms.values()) {
            if (room.peers.has(peerId)) {
                return room;
            }
        }

        return undefined;
    }

    /**
     * Retrieves all the active rooms as an array.
     *
     * @returns An array of all active `IRoom` objects.
     */
    getRooms(): IRoom[] {
        return Array.from(this.rooms.values());
    }

    /**
     * Retrieves a specific room by its ID.
     *
     * @param roomId - The unique identifier of the room to retrieve.
     * @returns The `IRoom` object or `undefined` if no room was found.
     */
    getRoom(roomId: string): IRoom | undefined {
        return this.rooms.get(roomId);
    }

    /**
     * Sets up all necessary listeners for a room (e.g., audio level observer).
     *
     * @param room - The room to set up listeners for.
     */
    establishRoomListeners(room: IRoom): void {
        if (!room.audioLevelObserver) {
            logger.warn(`Room ${room.id} does not have an audio level observer.`);
            return;
        }

        room.audioLevelObserver.on('volumes', async (volumes) => {
            for (const volumeInfo of volumes) {
                const { producer, volume } = volumeInfo;
                await this.activeSpeakerManager.handleVolumeUpdate(room, producer.id, volume);
            }
        });
    }

}
