import { Col, Layout, Row } from "antd";
import React, { useCallback, useEffect, useRef, useState } from "react";

import useMediaConsumers from "../hooks/media/UseMediaConsumers";
import useMediaProducers from "../hooks/media/UseMediaProducers";
import useTransports from "../hooks/transports/UseTransports";
import { useSocket } from "../providers/SocketProvider";
import { usePeerId } from "../store/Selectors";

import { useNavigate } from "react-router-dom";
import MeetingControls from "../components/meeting/MeetingControls";
import MenuSidebar from "../components/menu/MenuSidebar";
import AudioStream from "../components/streams/AudioStream";
import VideoStream from "../components/streams/VideoStream";
import useMediaMute from "../hooks/media/UseMediaMute";

import EndCallModal from "../components/modals/EndCallModal";
import { usePreferredCamera, usePreferredMic } from "../store/Selectors";

const { Content, Footer } = Layout;

const MeetingPage: React.FC = () => {
  const preferredMic = usePreferredMic();
  const preferredCamera = usePreferredCamera();  

  const peerId = usePeerId();
  const socket = useSocket();
  const navigate = useNavigate();

  // Saves the active producers in the room for visual feedback
  const [activeSpeakers, setActiveSpeakers] = useState<Set<string>>(new Set());

  const [isInitialized, setIsInitialized] = useState(false);
  const [localStream, setLocalStream] = useState<MediaStream | null>(null);
  const localStreamRef = useRef<MediaStream | null>(null);

  const { sendTransport, receiveTransport, isReady: transportsReady, networkStats } = useTransports();
  const { consumeMedia, removeConsumersByProducerId, videoStreams, audioStreams } = useMediaConsumers(receiveTransport!);
  const { produceTracks, producers, areProducersReady } = useMediaProducers(sendTransport!);
  const {isAudioMuted, isVideoMuted, toggleMute} = useMediaMute(producers, areProducersReady);

  const [isEndCallModalVisible, setIsEndCallModalVisible] = useState(false);
  const [countdown, setCountdown] = useState(3);

  const transitionStyles = {
    videoGrid: {
      transition: "all 0.5s ease-in-out", 
    },
    localStream: {
      transition: "transform 0.5s ease-in-out, opacity 0.5s ease-in-out",
    },
  };

  /**
   * Initializes media (audio/video streams) based on preferred devices
   */
  const initializeMediaStream = useCallback(async () => {
    try {
      const mediaStream = await navigator.mediaDevices.getUserMedia({
        audio: { deviceId: preferredMic ? { exact: preferredMic } : undefined },
        video: { deviceId: preferredCamera ? { exact: preferredCamera } : undefined }
      });
      localStreamRef.current = mediaStream;
      setLocalStream(mediaStream);
      return mediaStream;
    } catch (error) {
      console.error("Failed to initialize media stream:", error);
      throw error;
    }
  }, [preferredMic, preferredCamera]);  

  /**
   * Handles sending stream tracks to other users
   */
  const handleMediaProduction = useCallback(
    async (mediaStream: MediaStream) => {
      await produceTracks(mediaStream);
    },
    [produceTracks]
  );
  
  /**
   * Consumes all existing other user's streams
   */
  const consumeExistingProducers = useCallback(async () => {
    const producerIds: string[] = await socket.emitWithAck("getProducers", peerId);
    for (const producerId of producerIds) {
      await consumeMedia(producerId);
    }
  }, [socket, peerId, consumeMedia]);
  
  /**
   * Effect to initialize media streams, handle media production and consume existing producers
   */
  useEffect(() => {
    if (isInitialized) {
      console.log("Media already initialized");
      return;
    }
  
    if (!transportsReady) {
      console.log("Waiting for transports to be initialized...");
      return;
    }
  
    const initializeMedia = async () => {
      try {
        const mediaStream = await initializeMediaStream();
        await handleMediaProduction(mediaStream);
        await consumeExistingProducers();
        setIsInitialized(true);
      } catch (error) {
        console.error("Error initializing media:", error);
      }
    };
  
    initializeMedia();
  }, [isInitialized, transportsReady, initializeMediaStream, handleMediaProduction, consumeExistingProducers]);

  /**
   * Effect to producer active speaker events
   */
  useEffect(() => {
    const handleSpeaking = (producerId: string) => {
      setActiveSpeakers((prevSpeakers) => {
        if (prevSpeakers.has(producerId)) return prevSpeakers;
    
        const updatedSpeakers = new Set(prevSpeakers);
        updatedSpeakers.add(producerId);
        return updatedSpeakers;
      });
    };

    const handleInactiveSpeaker = (producerId: string) => {
      setActiveSpeakers((prevSpeakers) => {
        if (!prevSpeakers.has(producerId)) {
          return prevSpeakers;
        }    
        const updatedProducers = new Set(prevSpeakers);
        updatedProducers.delete(producerId);
        return updatedProducers;
      });
    }

    socket.on("activeSpeaker", handleSpeaking);
    socket.on("inactiveSpeaker", handleInactiveSpeaker);
    return () => {
      socket.off("activeSpeaker", handleSpeaking);
      socket.off("inactiveSpeaker", handleInactiveSpeaker);
    };
  }, [socket, activeSpeakers]);
  
  /** 
   * Effect to manage muting/unmuting the local audio and video tracks when the mute state changes.
   */
  useEffect(() => {
    if (!localStream) {
      console.log("Local stream not initialized yet");
      return;
    }

    localStream.getAudioTracks().forEach((track) => {
      track.enabled = !isAudioMuted;
    });

    localStream.getVideoTracks().forEach((track) => {
      track.enabled = !isVideoMuted;
    });
  }, [isAudioMuted, isVideoMuted, localStream]);

  /**
   * Effect to handle the case when a new producer is ready to be consumed
   */
  useEffect(() => {
    const handleProducerReady = async ({ producerId }: { producerId: string }) => {
      await consumeMedia(producerId);
    };
  
    socket.on("producerReady", handleProducerReady);
  
    return () => {
      socket.off("producerReady", handleProducerReady);
    };
  }, [socket, consumeMedia]);

  /**
   * Effect to handle the case when a peer is disconnected
   */
  useEffect(() => {
    const handlePeerDisconnected = ({ producerIds }: { peerId: string; producerIds: string[] }) => {
      removeConsumersByProducerId(producerIds);
    };
  
    socket.on("peerDisconnected", handlePeerDisconnected);
  
    return () => {
      socket.off("peerDisconnected", handlePeerDisconnected);
    };
  }, [socket, removeConsumersByProducerId]);  

  /**
   * Effect to handle the case when the user ends the call
   */
  const handleEndCall = () => {
    if (localStreamRef.current) {
      localStreamRef.current.getTracks().forEach((track) => track.stop());
      localStreamRef.current = null;
    }
    socket.emit("leaveRoom");
    setIsEndCallModalVisible(true);
  };

  /** 
   * Effect to manage the countdown on the end call modal. Once the countdown hits 0, redirects 
   * the user back to the home screen.
   */
  useEffect(() => {
    let timer: ReturnType<typeof setInterval>;
    if (isEndCallModalVisible) {
        timer = setInterval(() => {
            setCountdown((prevCountdown) => {
                if (prevCountdown <= 1) {
                    clearInterval(timer);
                    navigate("/", { replace: true });
                    return 0;
                }
                return prevCountdown - 1;
            });
        }, 500);
    }
    return () => {
        if (timer) clearInterval(timer);
    };
}, [isEndCallModalVisible, navigate]);

  return (
    <Layout className="h-screen bg-gray-700 overflow-hidden">
      <MenuSidebar />
      <Layout className="ml-[50px]">
        <Content className="flex justify-center items-center relative p-5 h-full bg-gray-700 overflow-hidden">
        <Row justify="center" align="middle" className="w-full h-[calc(100vh-160px)] relative">
            <Col
              className={`absolute top-[10px] bottom-[80px] w-[90%] h-[90%] rounded-lg overflow-hidden shadow-lg border border-white ${
                videoStreams.length > 1 ? "grid" : "" 
              }`}
              style={{
                ...transitionStyles.videoGrid,
                display: "grid",
                gridTemplateColumns: `repeat(auto-fit, minmax(200px, 1fr))`,
                gridAutoRows: "auto",
                gap: "10px", 
                alignItems: "center",
                justifyItems: "center",
              }}
            >
              {videoStreams.map(({ producerId, stream, producerState }) => {     
                  return (
                      <VideoStream 
                          key={producerId} 
                          stream={stream}
                          isVideoMuted={producerState === "paused"}
                          isActiveSpeaker={activeSpeakers.has(producerId)}
                      />
                  );
              })}
              {/* Render all received audio streams */}
              {audioStreams.map(({ producerId, stream }) => (
                <AudioStream key={producerId} stream={stream} />
              ))}
            </Col>
            <Col className={`absolute bottom-0 right-0 w-[200px] h-[120px] rounded-lg overflow-hidden border-2 border-white shadow-md ${
                videoStreams.length === 0 ? "hidden" : ""
              }`}>
              <div className="relative">
                {localStream && <VideoStream stream={localStream} isLocal={true} />}
                <div className="absolute top-2 right-2 z-10 flex flex-col items-end">
                  <span className="text-white text-xs font-bold">You</span>
                  <span
                    className="text-white text-xs"
                    style={{
                      color: "#fff",
                    }}
                    >
                    {networkStats.quality}
                  </span>
                </div>
              </div>
            </Col>

            {/* When there's only one participant, display local stream fullscreen */}
            {videoStreams.length === 0 && localStream && (
              <Col className="w-full h-full relative " style={transitionStyles.localStream}>
                <VideoStream stream={localStream} isLocal={true} />
              </Col>
            )}
          </Row>
        </Content>

        <Footer className="bg-gray-700 border-t border-white p-2 absolute bottom-0 w-full">
          <MeetingControls
            isMuted={isAudioMuted}
            isVideoEnabled={isVideoMuted}
            onToggleMute={toggleMute}
            onEndCall={handleEndCall}
          />
        </Footer>
      </Layout>

      <EndCallModal visible={isEndCallModalVisible} countdown={countdown} />
    </Layout>
  );
};

export default MeetingPage;