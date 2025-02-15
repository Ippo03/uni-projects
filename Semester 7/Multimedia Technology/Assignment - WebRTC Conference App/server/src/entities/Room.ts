import * as mediasoup from "mediasoup";
import { IPeer } from "./Peer";

export interface IRoom {
  id: string;
  router: mediasoup.types.Router;
  peers: Map<string, IPeer>;
  audioLevelObserver?: mediasoup.types.AudioLevelObserver;
  producerAudioConsumersMuted: Set<string>;
  worker: mediasoup.types.Worker;
}

