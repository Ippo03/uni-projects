import { Producer } from "mediasoup-client/lib/types";
import { useCallback, useEffect, useState } from "react";
import { useSocket } from "../../providers/SocketProvider";
import { findProducerByType } from "./utils/MediaUtils";

import { useIsCameraOn, useIsMuted } from "../../store/Selectors";

interface ToggleMuteResponse {
  toggled: boolean;
  error?: string;
}

const useMediaMute = (
  producers: Producer[],
  areProducersReady: boolean,
) => {
  const isMuted = useIsMuted(); 
  const isCameraOn = useIsCameraOn();

  const [isAudioMuted, setIsAudioMuted] = useState(isMuted);
  const [isVideoMuted, setIsVideoMuted] = useState(isCameraOn);
  const [initialSyncDone, setInitialSyncDone] = useState(false);
  const socket = useSocket();

  const emitToggleMute = useCallback(
    async (producerId: string, isMuted: boolean, type: "audio" | "video") => {
      try {
        const response: ToggleMuteResponse = await socket.emitWithAck("toggleProducer", {
          producerId,
          isMuted,
        });
        if (!response.toggled) {
          console.error(`Error toggling ${type} mute:`, response.error);
        }
        console.log(`Toggled ${type} mute to ${isMuted}`);
      } catch (error) {
        console.error(`Socket emit error (${type} mute):`, error);
      }
    },
    [socket]
  );

  const toggleMute = useCallback(
    async (type: "audio" | "video") => {

      const isCurrentlyMuted = type === "audio" ? isAudioMuted : isVideoMuted;
      const producer = findProducerByType(producers, type);

      if (!producer) {
        console.warn(`No ${type} producer found to toggle mute.`);
        return;
      }

      const newMuteState = !isCurrentlyMuted;
      console.log('New mute state for type ', type, newMuteState);

      if (type === "audio") {
        setIsAudioMuted(newMuteState);
      } else {
        setIsVideoMuted(newMuteState);
      }
      await emitToggleMute(producer.id, newMuteState, type);
    },
    [isAudioMuted, isVideoMuted, producers, emitToggleMute]
  );

  // Sync mute status with server on initial load
  useEffect(() => {
    if (initialSyncDone || !areProducersReady) {
      return;
    }
    // If we are not muted, we don't need to sync
    if (!isAudioMuted && !isVideoMuted) {
      setInitialSyncDone(true);
      return;
    }
  
    const syncProducers = async () => {
      try {
        await Promise.all(
          producers.map(async (producer) => {
            const type = producer.track?.kind as "audio" | "video";
            const isMuted = type === "audio" ? isAudioMuted : isVideoMuted;
            await emitToggleMute(producer.id, isMuted, type);
          })
        );
      } catch (error) {
        console.error("Error during producer synchronization:", error);
      } finally {
        setInitialSyncDone(true);
      }
    };
  
    syncProducers();
  }, [
    producers,
    initialSyncDone,
    emitToggleMute,
    isAudioMuted,
    isVideoMuted,
    areProducersReady,
  ]);
  
  return {
    isAudioMuted,
    isVideoMuted,
    toggleMute,
  };
};

export default useMediaMute;