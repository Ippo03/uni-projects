import { Transport } from "mediasoup-client/lib/types";
import { useCallback, useEffect, useRef, useState } from "react";
import { usePeerId } from "../../store/Selectors";
import useWebRtcTransport from "./UseWebRTCTransportHook";

interface NetworkStats {
  quality: 'Excellent' | 'Good' | 'Fair' | 'Poor' | 'Unknown';
  rtt?: number;
}
const useTransports = () => {
  const initializationInProgress = useRef(false);
  const transportsInitialized = useRef(false);
  const sendTransportRef = useRef<Transport | null>(null);
  const receiveTransportRef = useRef<Transport | null>(null);
  const statsIntervalRef = useRef<NodeJS.Timeout>();
  
  const [networkStats, setNetworkStats] = useState<NetworkStats>({
    quality: 'Unknown',
    rtt: 0
  });
  
  const [sendTransport, setSendTransport] = useState<Transport | null>(null);
  const [receiveTransport, setReceiveTransport] = useState<Transport | null>(null);
  const [isReady, setIsReady] = useState(false);
  
  const peerId = usePeerId();
  const { createTransport } = useWebRtcTransport();

  const calculateQuality = (rtt: number): NetworkStats['quality'] => {
    // There are times that this is 0 because the frontend
    // and the backend are on the same machine
    if (rtt === 0) return 'Excellent';
    if (rtt < 150) return 'Excellent';
    if (rtt < 250) return 'Good';
    if (rtt < 400) return 'Fair';
    return 'Poor';
  };

  const monitorNetworkStats = useCallback(async () => {
    if (!sendTransportRef.current) return;
    
    try {
      const stats = await sendTransportRef.current.getStats();
      const values = Array.from(stats.values());
      
      // Measures purely network travel time without processing time
      const candidatePair = values.find(stat => stat.type === 'candidate-pair' && stat.state === 'succeeded' &&
        stat.nominated // best candidate pair for communication
      );
  
      if (candidatePair) {
        const rtt = candidatePair.currentRoundTripTime ? candidatePair.currentRoundTripTime * 1000 : 0;
        const quality = calculateQuality(rtt);
        
        console.log('Current RTT:', rtt, 'ms');
        setNetworkStats({
          quality,
          rtt
        });
      }
    } catch (error) {
      console.error('Error getting transport stats:', error);
      setNetworkStats({
        quality: 'Unknown',
        rtt: 0
      });
    }
  }, []);


  const initTransports = useCallback(async () => {
    if (!peerId) {
      throw new Error("Peer ID is not available");
    }
    
    if (initializationInProgress.current || transportsInitialized.current) {
      return;
    }

    try {
      initializationInProgress.current = true;

      if (!sendTransportRef.current) {
        const createdSendTransport = await createTransport("send");
        sendTransportRef.current = createdSendTransport;
        setSendTransport(createdSendTransport);
      }

      if (!receiveTransportRef.current) {
        const createdReceiveTransport = await createTransport("receive");
        receiveTransportRef.current = createdReceiveTransport;
        setReceiveTransport(createdReceiveTransport);
      }

      transportsInitialized.current = true;
      setIsReady(true);
    } catch (error) {
      console.error("Error initializing transports:", error);
      transportsInitialized.current = false;
      sendTransportRef.current = null;
      receiveTransportRef.current = null;
      setSendTransport(null);
      setReceiveTransport(null);
      setIsReady(false);
    } finally {
      initializationInProgress.current = false;
    }
  }, [peerId, createTransport]);

  useEffect(() => {
    if (!transportsInitialized.current && peerId) {
      initTransports();
    }

    return () => {
      if (statsIntervalRef.current) {
        clearInterval(statsIntervalRef.current);
      }
      if (sendTransportRef.current) {
        sendTransportRef.current.close();
      }
      if (receiveTransportRef.current) {
        receiveTransportRef.current.close();
      }
      setIsReady(false);
    };
  }, [initTransports, peerId]);

  useEffect(() => {
    if (isReady && sendTransportRef.current) {
      // Initial check
      monitorNetworkStats();
      // Set up interval
      statsIntervalRef.current = setInterval(monitorNetworkStats, 2000);

      return () => {
        if (statsIntervalRef.current) {
          clearInterval(statsIntervalRef.current);
        }
      };
    }
  }, [isReady, monitorNetworkStats]);

  return {
    sendTransport,
    receiveTransport,
    isReady,
    networkStats,
  };
};

export default useTransports;