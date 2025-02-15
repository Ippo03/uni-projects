import { Producer } from "mediasoup-client/lib/types";

export const findProducerByType = (producers: Producer[], type: "audio" | "video"): Producer | undefined => {
  return producers.find((p) => p.track?.kind === type);
};