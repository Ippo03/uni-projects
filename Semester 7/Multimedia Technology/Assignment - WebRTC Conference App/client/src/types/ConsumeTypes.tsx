import { RtpParameters } from "mediasoup-client/lib/RtpParameters";

export interface ConsumeResponse {
  id: string;
  kind: string;
  rtpParameters: RtpParameters;
  producerState: 'active' | 'paused';
  error?: string;
}

export interface ConsumeResumeResponse {
  resumed: boolean;
  error?: string;
}