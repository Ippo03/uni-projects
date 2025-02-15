import { Transport } from "mediasoup-client/lib/types";
import { useCallback } from "react";
import { useDevice } from "../../providers/DeviceProvider";
import { useSocket } from "../../providers/SocketProvider";
import {
  TransportConnectResponse,
  TransportCreateResponse,
  TransportKind,
  TransportProduceResponse,
} from "../../types/TransportTypes";

const useWebRtcTransport = () => {
  const socket = useSocket();
  const { device } = useDevice();

  const handleTransportConnect = useCallback(async (transport: Transport, kind: TransportKind) => {
    transport.on("connect", async ({ dtlsParameters }, callback, errback) => {
      try {
        console.log(`Connecting ${kind} transport...`);
        const connectResponse: TransportConnectResponse = await socket.emitWithAck(
          "connectTransport",
          {
            transportId: transport.id,
            dtlsParameters,
          }
        );

        if (connectResponse.error) throw new Error(connectResponse.error);
        callback();
        console.log(`${kind} transport connected successfully`);
      } catch (error) {
        console.error(`Error connecting ${kind} transport:`, error);
        errback(new Error("Error connecting transport"));
      }
    });
  }, [socket]);

  const handleTransportProduce = useCallback(async (transport: Transport) => {
    transport.on("produce", async ({ kind, rtpParameters }, callback, errback) => {
      try {
        const produceResponse: TransportProduceResponse = await socket.emitWithAck(
          "produce",
          {
            transportId: transport.id,
            kind,
            rtpParameters,
          }
        );

        if (produceResponse.error) throw new Error(produceResponse.error);
        callback({ id: produceResponse.id });
        console.log(`${kind} media produced successfully`);
      } catch (error) {
        console.error(`Error producing ${kind} media:`, error);
        errback(new Error("Error producing media"));
      }
    });
  }, [socket]);

  const establishTransportListeners = useCallback((transport: Transport, kind:TransportKind) => {
    transport.on("connectionstatechange", (state) => {
      console.log(`Transport of kind ${kind} connection state changed to: ${state}`);
    });
  }, []);

  const createTransport = useCallback(async (kind: TransportKind): Promise<Transport | null> => {
    if (!device) {
      throw new Error("Device is not available");
    }

    try {
      console.log(`Creating ${kind} WebRTC transport...`);
      const response: TransportCreateResponse = await socket.emitWithAck("createWebRtcTransport");
      if (response.error) throw new Error(response.error);

      const transport =
        kind === "send"
          ? device.createSendTransport(response)
          : device.createRecvTransport(response);

      handleTransportConnect(transport, kind);

      if (kind === "send") {
        handleTransportProduce(transport);
      }
      
      establishTransportListeners(transport, kind);

      console.log(`Successfully created ${kind} transport with ID: ${transport.id}`);
      return transport;
    } catch (error) {
      console.error(`Error creating ${kind} WebRTC transport:`, error);
      throw error;
    }
  }, [device, socket, handleTransportConnect, handleTransportProduce, establishTransportListeners]);

  return { createTransport };
};

export default useWebRtcTransport;