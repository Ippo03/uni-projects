import {Socket} from 'socket.io';

import {RoomManager} from "./RoomManager";
import {IServerConfig} from "../util/configLoader";
import {logger} from "../util/logger";
import {generateTransport, getProducerState} from "../util/mediasoup/mediasoupUtils";


export function handleConnection(serverConfig: IServerConfig, socket: Socket, roomManager: RoomManager): void {

    /**
     * Handles the creation of a new room.
     *
     * @param roomId The ID of the room to be created.
     * @param callback A callback function to return the result (room ID or error message).
     */
    socket.on('createRoom', async (roomId: string, callback) => {
        try {
            const room = await roomManager.createRoom(roomId);
            callback({roomId: room.id});
        } catch (error) {
            logger.error('Error on createRoom:', error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Handles a peer joining an existing room.
     *
     * @param roomId The ID of the room to join.
     * @param callback A callback function to return the peer's RTP capabilities and peer ID, or an error message.
     */
    socket.on('joinRoom', async (roomId: string, callback) => {
        try {
            const room = roomManager.getRoom(roomId);
            if (!room) {
                callback({error: 'Room does not exist'});
                return;
            }


            await roomManager.addPeer(roomId, socket.id, socket);
            callback({
                rtpCapabilities: room.router.rtpCapabilities,
                peerId: socket.id
            });

            socket.to(roomId).emit('peerJoined', {peerId: socket.id});
            socket.join(roomId);
        } catch (error) {
            logger.error('Error on joinRoom:', error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Handles a peer leaving a room.
     */
    socket.on('leaveRoom', async () => {
        try {
            const room = await roomManager.getPeerRoom(socket.id);

            if (room) {
                const producerIds = Array.from(room.peers.get(socket.id)?.producers.keys() || []);

                socket.to(room.id).emit('peerDisconnected', {
                    peerId: socket.id,
                    producerIds: producerIds,
                });

                await roomManager.removePeer(room.id, socket.id);
            }
        } catch (error) {
            logger.error('Error on leaveRoom:', error);
        }
    });

    /**
     * Handles socket disconnection and performs cleanup (if required).
     */
    socket.on('disconnect', () => {
        try {
            socket.emit('leaveRoom');
            logger.info('Socket disconnected:', socket.id);
        } catch (error) {
            logger.error('Error on socket disconnect:', error);
        }
    });

    /**
     * Creates a WebRTC transport for a peer.
     *
     * @param callback A callback function to return the transport details (e.g., ID, ICE parameters) or an error message.
     */
    socket.on('createWebRtcTransport', async (callback) => {
        try {
            const room = findPeerRoom(socket.id);
            if (!room) {
                throw new Error('Peer not found in any room');
            }
            const transport = await generateTransport(room, serverConfig);

            const peer = room.peers.get(socket.id);
            if (!peer) {
                throw new Error('Peer not found');
            }

            peer.transports.set(transport.id, transport);

            callback({
                id: transport.id,
                iceParameters: transport.iceParameters,
                iceCandidates: transport.iceCandidates,
                dtlsParameters: transport.dtlsParameters
            });
            logger.info(`WebRtcTransport created for peer ${socket.id}`);
        } catch (error) {
            logger.error('Error creating WebRtcTransport:', error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Connects a WebRTC transport using DTLS parameters.
     *
     * @param transportId The ID of the transport to be connected.
     * @param dtlsParameters The DTLS parameters for the connection.
     * @param callback A callback to indicate success or return an error message.
     */
    socket.on('connectTransport', async ({transportId, dtlsParameters}, callback) => {
        try {
            const peer = findPeer(socket.id);
            if (!peer) {
                throw new Error('Peer not found');
            }

            const transport = peer.transports.get(transportId);
            if (!transport) {
                throw new Error('Transport not found');
            }

            await transport.connect({dtlsParameters});
            logger.info(`Transport connected for peer ${socket.id}`);
            callback({connected: true});
        } catch (error) {
            logger.error('Error connecting transport:', error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Handles the production of media by a peer.
     *
     * @param transportId The ID of the transport being used to produce media.
     * @param kind The kind of media (e.g., audio or video).
     * @param rtpParameters The RTP parameters for the media.
     * @param callback A callback to return the producer ID or an error message.
     */
    socket.on('produce', async ({transportId, kind, rtpParameters}, callback) => {
        try {
            const peer = findPeer(socket.id);
            const peerRoom = findPeerRoom(socket.id);
            if (!peer) {
                throw new Error('Peer not found');
            }

            logger.info("Received produce for kind ", kind);
            if (!peerRoom) {
                throw new Error('Room not found');
            }

            const transport = peer.transports.get(transportId);
            if (!transport) {
                throw new Error('Transport not found');
            }

            const producer = await transport.produce({
                kind,
                rtpParameters
            });

            // Register audio producer to audio level observer
            if (kind === 'audio' && peerRoom.audioLevelObserver) {
                peerRoom.audioLevelObserver.addProducer({
                    producerId: producer.id,
                });
                logger.info(`Producer ${producer.id} added to AudioLevelObserver in room ${peerRoom.id}`);
            }

            peer.producers.set(producer.id, producer);
            logger.info(`Producer created for peer ${socket.id}`);

            socket.to(peerRoom.id).emit('producerReady', {producerId: producer.id, peerId: socket.id});
            callback({id: producer.id});
        } catch (error) {
            logger.error('Error creating producer:', error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Handles the creation of a consumer for consuming media from a producer.
     *
     * @param producerId The ID of the producer to consume from.
     * @param rtpCapabilities The RTP capabilities of the peer.
     * @param receiveTransportId The ID of the receiving transport.
     * @param callback A callback to return the consumer details or an error message.
     */
    socket.on('consume', async ({producerId, rtpCapabilities, receiveTransportId}, callback) => {
        try {
            const peer = findPeer(socket.id);
            if (!peer) {
                throw new Error('Peer not found.');
            }

            const room = findPeerRoom(socket.id);
            if (!room) {
                throw new Error('Room not found');
            }

            if (!room.router.canConsume({
                producerId,
                rtpCapabilities,
            })) {
                throw new Error('Cannot consume');
            }

            const transport = peer.transports.get(receiveTransportId);
            if (!transport) {
                throw new Error('Transport not found');
            }

            const consumer = await transport.consume({
                producerId,
                rtpCapabilities,
                paused: true,
            });
            const producerState = getProducerState(room, producerId);

            peer.consumers.set(consumer.id, consumer);
            logger.info(`Consumer created for peer ${socket.id}`);
            callback({
                id: consumer.id,
                producerId,
                kind: consumer.kind,
                rtpParameters: consumer.rtpParameters,
                producerState: producerState,
            });
        } catch (error) {
            logger.error(`[Socket ${socket.id}] Error creating consumer:`, error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Toggles the enabled state of a producer (audio or video).
     *
     * Pausing a producer will stop the flow of media data from the producer to the router.
     *
     * @param producerId The ID of the producer to toggle.
     * @param enabled The new state (true to unmute/resume, false to mute/pause).
     * @param callback A callback to confirm the toggle action or return an error message.
     */
    socket.on('toggleProducer', async ({producerId, isMuted}, callback) => {
        try {
            const peer = findPeer(socket.id);
            if (!peer) {
                throw new Error('Peer not found');
            }

            const producer = peer.producers.get(producerId);
            if (!producer) {
                throw new Error('Producer not found');
            }

            const room = findPeerRoom(socket.id);
            if (!room) {
                throw new Error('Room not found');
            }

            if (isMuted) {
                await producer.pause();
                logger.info(`Producer with ID ${producerId} paused for peer ${socket.id}`);
            } else {
                await producer.resume();
                logger.info(`Producer with ID ${producerId} resumed for peer ${socket.id}`);
            }
            socket.to(room.id).emit('producerStateChanged', {
                producerId,
                producerState: isMuted ? 'paused' : 'active',
            });
            logger.info(`Informing room ${room.id} of producer state change`);

            callback({toggled: true, isMuted});
        } catch (error) {
            logger.error(`[Socket ${socket.id}] Error toggling producer:`, error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Resumes a paused consumer.
     *
     * @param consumerId The ID of the consumer to resume.
     * @param peerId The ID of the peer associated with the consumer.
     * @param callback A callback to confirm that the consumer has resumed or return an error message.
     */
    socket.on('resumeConsumer', async ({consumerId, peerId}, callback) => {
        try {
            const peer = findPeer(peerId);
            if (!peer) {
                throw new Error('Peer not found');
            }

            const consumer = peer.consumers.get(consumerId);
            if (!consumer) {
                throw new Error('Consumer not found');
            }

            await consumer.resume();
            logger.info(`Consumer resumed for peer ${peerId}`);
            callback({resumed: true});
        } catch (error) {
            logger.error(`[Socket ${socket.id}]Error resuming consumer:`, error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    /**
     * Retrieves the list of producer IDs that the current peer can consume.
     *
     * @param peerId The ID of the current peer.
     * @param callback A callback to return the list of producer IDs or an error message.
     */
    socket.on('getProducers', (peerId: string, callback) => {
        try {
            const peer = findPeer(peerId);
            if (!peer) {
                throw new Error('Peer not found');
            }
            const curProducerIds = Array.from(peer.producers.keys());

            const room = findPeerRoom(peerId);
            if (!room) {
                throw new Error('Room not found');
            }

            const roomProducerIds = Array.from(room.peers.values())
                .flatMap(peer => Array.from(peer.producers.keys()))
                .filter(producerId => !curProducerIds.includes(producerId));

            callback(roomProducerIds);
        } catch (error) {
            logger.error('Error getting producers:', error);
            callback({error: error instanceof Error ? error.message : 'Unknown error'});
        }
    });

    function findPeerRoom(peerId: string) {
        for (const room of roomManager.getRooms()) {
            if (room.peers.has(peerId)) {
                return room;
            }
        }
        return undefined;
    }

    function findPeer(peerId: string) {
        const room = findPeerRoom(peerId);
        return room?.peers.get(peerId);
    }
}