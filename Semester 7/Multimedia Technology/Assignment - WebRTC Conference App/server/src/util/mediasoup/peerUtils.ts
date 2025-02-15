import { IPeer } from "../../entities/Peer";
import { IRoom } from "../../entities/Room";


/**
 * Finds the peer that owns the producer with the given ID.
 * @param room the room to search in
 * @param producerId the producer ID to search for
 * @returns the peer that owns the producer with the given ID, or undefined if no such peer exists
 * TODO: Maybe we can move the function to a Peer Entity class later since it's more related to that.
 */
export function getPeerByProducerId(room: IRoom, producerId: string): IPeer | undefined {
    for (const peer of room.peers.values()) {
        for (const producer of peer.producers.values()) {
            if (producer.id === producerId) {
                return peer;
            }
        }
    }
    return undefined;
}
  