import * as mediasoup from 'mediasoup';
import { Socket } from 'socket.io';

export interface IPeer {
    id: string;
    socket: Socket;
    displayName?: string;
    transports: Map<string, mediasoup.types.Transport>;
    producers: Map<string, mediasoup.types.Producer>;
    consumers: Map<string, mediasoup.types.Consumer>;
    rtpCapabilities?: mediasoup.types.RtpCapabilities;
}