import {IServerConfig} from "../configLoader";
import {IRoom} from "../../entities/Room";
import { WebRtcTransport } from "mediasoup/node/lib/types";
import {logger} from "../../util/logger";

/**
 * Creates a new WebRTC transport for the given room.
 * 
 * @param room the room to generate the transport for
 * @param serverConfig  the server configuration
 * @returns a Promise that resolves to the created transport
 */
export async function generateTransport(room: IRoom, serverConfig: IServerConfig): Promise<WebRtcTransport> {
    return await room.router.createWebRtcTransport({
        listenInfos: [
            {
                ip: '0.0.0.0',
                announcedAddress: serverConfig.announcedAddress,
                protocol: 'udp'
            },
            {
                ip: '0.0.0.0',
                announcedAddress: serverConfig.announcedAddress,
                protocol: 'tcp'
            }
        ],
        enableUdp: true,
        enableTcp: true,
        preferUdp: true,
        initialAvailableOutgoingBitrate: 1000000
    });
}

// TODO: Maybe we can move the functions to a more appropriate place like a producerUtils.ts file or class
/**
* Mutes a producer by pausing all associated consumers.
*
* This will stop the consumer from receiving media data from the producer.
*
* Does not affect the producer; media data continues to flow from the producer to the router and to other consumers
* that are not paused.
*
* @param roomId - The ID of the room containing the producer.
* @param producerId - The ID of the producer to mute.
*/
export async function muteProducerConsumers(room: IRoom, producerId: string): Promise<void> {
    room.producerAudioConsumersMuted.add(producerId);

    // Find each peer that consumes this producer and pause the consumer
    for (const peer of room.peers.values()) {
        for (const consumer of peer.consumers.values()) {
            if (consumer.producerId === producerId && !consumer.paused) {
                await consumer.pause();
                logger.debug(`Consumer ${consumer.id} paused for muted producer ${producerId}.`);
            }
        }
    }
}

/**
* Unmutes a producer by resuming all associated consumers.
*
* @param roomId - The ID of the room containing the producer.
* @param producerId - The ID of the producer to unmute.
*/
export async function unmuteProducerConsumers(room: IRoom, producerId: string): Promise<void> {
    room.producerAudioConsumersMuted.delete(producerId);
    logger.debug(`Producer ${producerId} removed from mutedProducers in room ${room}.`);

    for (const peer of room.peers.values()) {
        for (const consumer of peer.consumers.values()) {
            if (consumer.producerId === producerId && consumer.paused) {
                await consumer.resume();
                logger.debug(`Consumer ${consumer.id} resumed for unmuted producer ${producerId}.`);
            }
        }
    }
}

/**
 * Get the producer state (paused/active) of the producer with the given ID.
 * @param room room to search in
 * @param producerId producer ID to search for
 * @returns the producer state (paused/active) of the producer with the given ID
 */
export function getProducerState(room: IRoom, producerId: string) {
    for (const peer of room.peers.values()) {
      const producer = peer.producers.get(producerId);
      if (producer) {
        return producer.paused ? 'paused' : 'active';
      }
    }
    logger.warn(`Producer with ID ${producerId} not found in room ${room.id}`);
    return 'active';
};
  