import { useEffect, useRef } from "react";

interface AudioStreamProps {
    stream: MediaStream;
};

const AudioStream: React.FC<AudioStreamProps> = ({ stream }) => {
    const audioRef = useRef<HTMLAudioElement>(null);
  
    useEffect(() => {
      if (!audioRef.current) {
        console.warn("Audio ref not set");
        return;
      }
      if (audioRef.current.srcObject) {
        return;
      }
      audioRef.current.srcObject = stream;
    }, [stream]);

    return (
      <audio
        key={stream.id}
        autoPlay
        controls
        ref={audioRef}
        style={{
          display: "none",
        }}
      />
    );
 };

export default AudioStream;