import { DtlsParameters, IceCandidate, IceParameters } from "mediasoup-client/lib/types";

export type TransportKind = "send" | "receive";

export type TransportCreateResponse = {
  id: string;
  iceParameters: IceParameters;
  iceCandidates: IceCandidate[];
  dtlsParameters: DtlsParameters;
  error?: string;
};

export type TransportConnectResponse = {
  connected: boolean;
  error?: string;
};

export type TransportProduceResponse = {
  id: string;
  error?: string;
};