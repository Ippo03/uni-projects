import React, { useEffect, useRef } from "react";

interface VideoProps {
  stream: MediaStream;
  isVideoMuted?: boolean;
  isActiveSpeaker?: boolean;
  isLocal?: boolean;
}

const VideoStream: React.FC<VideoProps> = ({ stream, isVideoMuted=false, isLocal=false, isActiveSpeaker=false }) => {
  const videoRef = useRef<HTMLVideoElement | null>(null);

  useEffect(() => {
    if (videoRef.current) {
      if (videoRef.current.srcObject !== stream) {
        videoRef.current.srcObject = stream;
      }
      if (!isLocal) {
        console.log('Is video muted', isVideoMuted);
      }
    } else {
      console.warn("Video ref not set");
    }
  }, [stream, isLocal, isVideoMuted]);

  const containerStyles: React.CSSProperties = {
    position: 'relative',
    width: '100%',
    height: '100%',
    ...(isLocal ? {
      border: "1px solid white",
    } : {
      borderRadius: "8px",
      border: isActiveSpeaker ? "4px solid rgb(21, 110, 211)" : "4px solid transparent",
    }),
  };

  const blurOverlayStyles: React.CSSProperties = {
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    backdropFilter: 'blur(50px)',
    transition: 'opacity 0.2s ease-in-out',
    opacity: isVideoMuted ? 1 : 0,
    pointerEvents: 'none',
    borderRadius: isLocal ? undefined : "8px",
  };

  const videoStyles: React.CSSProperties = {
    width: "100%",        
    height: "100%",       
    objectFit: "cover",
    ...(isLocal ? {} : {
      aspectRatio: "16/9",
      borderRadius: "8px",
    }),
  };

  return (
    <div style={containerStyles}>
      <video
        ref={videoRef}
        autoPlay
        playsInline
        muted={isLocal ? true : isVideoMuted}
        style={videoStyles}
        key={stream.id}
      />
      <div style={blurOverlayStyles} />
    </div>
  );
};

export default VideoStream;