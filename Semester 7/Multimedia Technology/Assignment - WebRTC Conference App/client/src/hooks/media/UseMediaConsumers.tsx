import { Transport } from "mediasoup-client/lib/types";
import { useCallback, useEffect, useRef, useState } from "react";
import { useDevice } from "../../providers/DeviceProvider";
import { useSocket } from "../../providers/SocketProvider";
import { usePeerId } from "../../store/Selectors";
import { ConsumeResponse, ConsumeResumeResponse } from "../../types/ConsumeTypes";

interface Stream {
 producerId: string;
 stream: MediaStream;
 producerState: "active" | "paused";
}

/**
* Hook to manage media consumers and handle stream state changes
* @param receiveTransport The transport used to receive media
*/
const useMediaConsumers = (receiveTransport: Transport) => {
  const [videoStreams, setVideoStreams] = useState<Stream[]>([]);
  const [audioStreams, setAudioStreams] = useState<Stream[]>([]);
  // Store state changes that arrive before the stream is created
  // Maps the producer ID to the state change that needs to be applied before consuming the stream
  const pendingStateChanges = useRef<Map<string, 'active' | 'paused'>>(new Map());

  const { device } = useDevice();
  const peerId = usePeerId()!;
  const socket = useSocket();
  const consumedProducersRef = useRef<Set<string>>(new Set());

  /**
    * Set up event listeners for media track events
    * @param track The MediaStreamTrack to attach listeners to
    */
  const establishTrackListeners = useCallback((track: MediaStreamTrack) => {
    track.onmute = () => console.log(`Track ${track.id} muted`);
    track.onunmute = () => console.log(`Track ${track.id} unmuted`);
    track.addEventListener("ended", () => {
      console.log(`Track ${track.id} ended`);
      track.stop();
    });
  }, []);

  /**
    * Create a consumer for a specific producer
    * @param producerId The ID of the producer to consume from
    * @returns Object containing the consumer, kind and producer state
    */
  const createConsumer = useCallback(
    async (producerId: string) => {
      const response: ConsumeResponse = await socket.emitWithAck("consume", {
        producerId,
        rtpCapabilities: device!.rtpCapabilities,
        receiveTransportId: receiveTransport.id,
      });

      const consumer = await receiveTransport.consume({
        id: response.id,
        producerId,
        kind: response.kind as "audio" | "video",
        rtpParameters: response.rtpParameters,
        appData: { peerId },
      });

      return { 
        consumer, 
        kind: response.kind, 
        producerState: response.producerState 
      };
    },
    [device, peerId, receiveTransport, socket]
  );

  /**
    * Resume a paused consumer
    * @param consumerId The ID of the consumer to resume
    */
  const resumeConsumer = useCallback(
    async (consumerId: string) => {
      const resp: ConsumeResumeResponse = await socket.emitWithAck("resumeConsumer", {
        consumerId,
        peerId,
      });
      if (!resp.resumed) {
        throw new Error(`Failed to resume consumer: ${consumerId}`);
      }
    },
    [peerId, socket]
  );

  /**
    * Create a consumer and add its stream to video/audio streams
    * Handles initial state and any pending state changes
    * @param producerId The ID of the producer to consume from
    */
  const consumeMedia = useCallback(
    async (producerId: string) => {
      if (!receiveTransport) {
        console.debug("Receive transport is not ready, waiting...");
        return;
      }
      if (!device) {
        throw new Error("Device not initialized");
      }

      if (consumedProducersRef.current.has(producerId)) {
        console.debug(`Already consuming media for producer: ${producerId}`);
        return;
      }

      consumedProducersRef.current.add(producerId);

      try {
        const { consumer, kind, producerState } = await createConsumer(producerId);
        const track = consumer.track;
        establishTrackListeners(track);

        const stream = new MediaStream([track]);
        
        // Check if there was a state (producer audio or video state change) while we were creating the consumer
        const pendingState = pendingStateChanges.current.get(producerId);
        const finalState = pendingState || producerState;
        pendingStateChanges.current.delete(producerId); // Clean up used pending state

        const streamWithState = { 
          producerId, 
          stream, 
          producerState: finalState 
        };

        console.log(`Created ${kind} consumer with ID: ${consumer.id}`);
        if (kind === "video") {
          setVideoStreams(prev => [...prev, streamWithState]);
        } else {
          setAudioStreams(prev => [...prev, streamWithState]);
        }

        await resumeConsumer(consumer.id);
        console.log(`Consumer resumed: ${consumer.id}`);
      } catch (error) {
        console.error("Error consuming media:", error);
      } finally {
        consumedProducersRef.current.delete(producerId);
      }
    },
    [createConsumer, device, establishTrackListeners, receiveTransport, resumeConsumer]
  );

  /**
    * Remove consumers for the given producer IDs and clean up any pending states
    * @param producerIds Array of producer IDs to remove
    */
  const removeConsumersByProducerId = useCallback((producerIds: string[]) => {
    for (const producerId of producerIds) {
      setVideoStreams(prev => prev.filter(stream => stream.producerId !== producerId));
      setAudioStreams(prev => prev.filter(stream => stream.producerId !== producerId));
      pendingStateChanges.current.delete(producerId);
    }
  }, []);

  /**
    * Handle producer state changes (active/paused)
    * If stream doesn't exist yet, store state change to apply when it's created
    */
  useEffect(() => {
    const handleProducerStateChange = ({ producerId, producerState }: { producerId: string; producerState: 'active' | 'paused' }) => {      
      // Handle state change for video streams
      setVideoStreams(prev => {
        const streamExists = prev.some(s => s.producerId === producerId);
        
        if (!streamExists) {
          // Store state change to apply when stream is created later
          pendingStateChanges.current.set(producerId, producerState);
          return prev;
        }
        
        return prev.map(stream => 
          stream.producerId === producerId 
            ? { ...stream, producerState } 
            : stream
        );
      });

      // Handle state change for audio streams
      setAudioStreams(prev => {
        const streamExists = prev.some(s => s.producerId === producerId);
        
        if (!streamExists) {
          pendingStateChanges.current.set(producerId, producerState);
          return prev;
        }
        
        return prev.map(stream => 
          stream.producerId === producerId 
            ? { ...stream, producerState } 
            : stream
        );
      });
    };

    socket.on('producerStateChanged', handleProducerStateChange);
    return () => {
      socket.off('producerStateChanged', handleProducerStateChange);
    };
  }, [socket]);
  

  return {
    consumeMedia,
    removeConsumersByProducerId,
    videoStreams,
    audioStreams,
  };
};

export default useMediaConsumers;