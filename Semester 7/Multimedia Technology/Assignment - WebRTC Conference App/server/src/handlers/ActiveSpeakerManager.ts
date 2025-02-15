import { logger } from "../util/logger";
import { IPeer } from '../entities/Peer';
import { IRoom } from "../entities/Room";
import { Producer } from 'mediasoup/node/lib/Producer';
import { audioLevelObserverConfig } from "../util/mediasoup/audioLevelObserverConfig";
import { muteProducerConsumers, unmuteProducerConsumers } from '../util/mediasoup/mediasoupUtils';
import { getPeerByProducerId } from "../util/mediasoup/peerUtils";

interface ActiveSpeakerInfo {
    producerId: string;
    lastSpeakingTime: number;
    videoProducerId: string;
}

export class ActiveSpeakerManager {
    private activeSpeakers: Map<string, Set<ActiveSpeakerInfo>> = new Map();
    // Minimum interval for considering a new speaker
    private readonly minSwitchInterval: number = 3000;
    // Prevents too many active speakers which could cause confusion on who is actually speaking
    private readonly maxActiveSpeakers: number = 5;

    constructor() {
        this.activeSpeakers = new Map();
    }

    /**
     * Handle volume update for a producer in a room
     * @param room the room to handle the volume update for
     * @param producerId the producer ID to handle the volume update for
     * @param volume the volume level to handle
     */
    async handleVolumeUpdate(room: IRoom, producerId: string, volume: number): Promise<void> {
        const peer = getPeerByProducerId(room, producerId);
        if (!peer) {
            logger.warn(`No peer found for producer ${producerId}`);
            return;
        }

        const videoProducerId = this.getVideoProducerId(peer);
        if (!videoProducerId) {
            logger.warn(`No video producer found for peer ${peer.id}`);
            return;
        }

        await this.handleVolumeLevels(room, producerId, videoProducerId, volume);
    }

    /**
     * Process volume levels and manage muting/unmuting
     * @param room the room to process the volume levels for
     * @param audioProducerId the audio producer ID to process the volume levels for
     * @param videoProducerId the video producer ID of the same producer, needed to be passed to the frontend
     */
    private async handleVolumeLevels(
        room: IRoom, 
        audioProducerId: string, 
        videoProducerId: string, 
        volume: number
    ): Promise<void> {
        logger.info(`Volume for producer ${audioProducerId} in room ${room.id}: ${volume}`);
        if (volume < audioLevelObserverConfig.muteThreshold) {
            await this.handleLowVolume(room, audioProducerId, videoProducerId);
        } else {
            await this.handleHighVolume(room, audioProducerId, videoProducerId);
        }
    }

    /**
     * Handle low volume scenario - mute and remove from active speakers
     * @param room the room to handle the low volume for
     * @param audioProducerId the audio producer ID to handle the low volume for
     * @param videoProducerId the video producer ID of the same producer, needed to be passed to the frontend
     */
    private async handleLowVolume(
        room: IRoom, 
        audioProducerId: string, 
        videoProducerId: string
    ): Promise<void> {
        if (!room.producerAudioConsumersMuted.has(audioProducerId)) {
            await muteProducerConsumers(room, audioProducerId);
            logger.debug(`Producer ${audioProducerId} muted due to low volume.`);
        }
        
        this.removeActiveSpeaker(room.id, audioProducerId);
        this.notifyRoomVoiceActivity(room, videoProducerId, false);
    }

    /**
     * Handle high volume scenario - unmute and potentially add to active speakers
     * @param room the room to handle the high volume for
     * @param audioProducerId the audio producer ID to handle the high volume for
     * @param videoProducerId the video producer ID of the same producer, needed to be passed to the frontend
     */
    private async handleHighVolume(
        room: IRoom, 
        audioProducerId: string, 
        videoProducerId: string, 
    ): Promise<void> {
        if (room.producerAudioConsumersMuted.has(audioProducerId)) {
            await unmuteProducerConsumers(room, audioProducerId);
            logger.debug(`Producer ${audioProducerId} resumed as volume is sufficient.`);
        }
        
        this.processActiveSpeaker(room, audioProducerId, videoProducerId);
    }

    /**
     * Process active speaker changes with management of multiple speakers
     */
    private processActiveSpeaker(
        room: IRoom, 
        audioProducerId: string, 
        videoProducerId: string, 
    ): void {
        if (!this.activeSpeakers.has(room.id)) {
            this.activeSpeakers.set(room.id, new Set());
        }
        const currentTime = Date.now();
        
        const roomSpeakers = this.activeSpeakers.get(room.id)!;
        const existingSpeaker = Array.from(roomSpeakers).find(s => s.producerId === audioProducerId);

        if (existingSpeaker) {
            // Update existing speaker's timestamp
            existingSpeaker.lastSpeakingTime = currentTime;
            this.notifyRoomVoiceActivity(room, videoProducerId, true);
            return;
        }

        if (this.canAddNewSpeaker(roomSpeakers, currentTime)) {
            this.addActiveSpeaker(room.id, {
                producerId: audioProducerId,
                videoProducerId,
                lastSpeakingTime: currentTime
            });
            this.notifyRoomVoiceActivity(room, videoProducerId, true);
        }
    }

    /**
     * Check if we can add a new speaker based on current speakers and timing
     * It checks if we have space for a new speaker and if enough time has passed since the last speaker talked
     * @param speakers the current active speakers
     * @param currentTime the current time in milliseconds
     * @returns true if a new speaker can be added, false otherwise
     */
    private canAddNewSpeaker(
        speakers: Set<ActiveSpeakerInfo>,
        currentTime: number
    ): boolean {
        if (speakers.size < this.maxActiveSpeakers) {
            return true;
        }

        // Check if enough time has passed since the oldest speaker's last update
        const oldestSpeaker = Array.from(speakers).sort((a, b) => a.lastSpeakingTime - b.lastSpeakingTime)[0];
        return currentTime - oldestSpeaker.lastSpeakingTime >= this.minSwitchInterval;
    }

    /**
     * Add a new active speaker, potentially removing the oldest one
     */
    private addActiveSpeaker(roomId: string, speakerInfo: ActiveSpeakerInfo): void {
        const speakers = this.activeSpeakers.get(roomId)!;
        
        if (speakers.size >= this.maxActiveSpeakers) {
            // Remove the oldest speaker
            const oldestSpeaker = Array.from(speakers).sort((a, b) => a.lastSpeakingTime - b.lastSpeakingTime)[0];
            speakers.delete(oldestSpeaker);
        }

        speakers.add(speakerInfo);
        logger.info(`Added active speaker ${speakerInfo.producerId} to room ${roomId}`);
    }

    /**
     * Remove a speaker from active speakers
     */
    private removeActiveSpeaker(roomId: string, producerId: string): void {
        const speakers = this.activeSpeakers.get(roomId);
        if (!speakers) return;

        const speakerToRemove = Array.from(speakers).find(s => s.producerId === producerId);
        
        if (speakerToRemove) {
            speakers.delete(speakerToRemove);
            logger.info(`Removed active speaker ${producerId} from room ${roomId}`);
        }
    }

    /**
     * Get the video producer ID for a peer
     */
    private getVideoProducerId(peer: IPeer): string | undefined {
        return Array.from(peer.producers.values()).find((producer: Producer) => producer.kind === 'video')?.id;
    }

    /**
     * Get current active speakers for a room
     */
    public getActiveSpeakers(roomId: string): ActiveSpeakerInfo[] {
        const speakers = this.activeSpeakers.get(roomId);
        return speakers ? Array.from(speakers) : [];
    }

    /**
     * Notify all peers in the room about voice activity
     */
    private notifyRoomVoiceActivity(room: IRoom, videoProducerId: string, active: boolean): void {
        const message = active ? 'activeSpeaker' : 'inactiveSpeaker';
        // TODO: Potentially doing too many emits here, could be optimized later
        // to do a batch emit for all peers in the room, or increase the interval
        for (const peer of room.peers.values()) {
            peer.socket.emit(message, videoProducerId);
        }
    }
}