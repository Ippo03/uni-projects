import { useState, useCallback } from "react";
import { Transport, Producer } from "mediasoup-client/lib/types";

const useMediaProducers = (sendTransport: Transport | null) => {
  const [producers, setProducers] = useState<Producer[]>([]);
  const [areProducersReady, setAreProducersReady] = useState(false);

  const produceTracks = useCallback(
    async (mediaStream: MediaStream) => {
      if (!sendTransport) {
        console.log("Send transport is not ready, waiting...");
        return;
      }
      if (!mediaStream) {
        console.log("Media stream is not ready, waiting...");
        return;
      }

      const newProducers = await Promise.all(
        mediaStream.getTracks().map(async (track) => {
          const encodings =
            track.kind === "video"
              ? [
                  { maxBitrate: 100000 }, // low
                  { maxBitrate: 300000 }, // medium
                  { maxBitrate: 900000 }, // high
                ]
              : [{ maxBitrate: 64000 }];
          const producer = await sendTransport.produce({
            track,
            encodings,
          });
          console.log(`Producing ${track.kind} track`);
          return producer;
        })
      );

      setProducers((prevProducers) => [...prevProducers, ...newProducers]);
      setAreProducersReady(true); // This is needed because we do not know how many tracks we will produce
    },
    [sendTransport]
  );

  return { producers, produceTracks, areProducersReady };
};

export default useMediaProducers;
