import { WorkerLogLevel, WorkerLogTag } from "mediasoup/node/lib/Worker";
import { RtpCodecCapability } from "mediasoup/node/lib/RtpParameters";
import * as os from "node:os";

export const mediasoupConfig = {
  mediasoup: {
    worker: {
      numWorkers: os.cpus.length,
      rtcMinPort: 40000,
      rtcMaxPort: 41000,
      logLevel: "warn" as WorkerLogLevel,
      logTags: ["info", "ice", "dtls", "rtp", "srtp", "rtcp"] as WorkerLogTag[],
    },
    router: {
      mediaCodecs: [
        {
          kind        : "audio",
          mimeType    : "audio/opus",
          clockRate   : 48000,
          channels    : 2
        },
        {
          kind       : "video",
          mimeType   : "video/H264",
          clockRate  : 90000,
          parameters :
          {
            "packetization-mode"      : 1,
            "profile-level-id"        : "42e01f",
            "level-asymmetry-allowed" : 1
          }
        },
        {
          kind       : "video",
          mimeType   : "video/VP8",
          clockRate  : 90000,
          parameters : { }
        },
      ] as RtpCodecCapability[],
    },
  },
};
