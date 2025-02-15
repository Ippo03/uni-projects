import { useState, useEffect } from 'react';

interface MediaDevice {
  deviceId: string;
  kind: 'audioinput' | 'audiooutput' | 'videoinput';
  label: string;
}

export const useMediaDevices = () => {
  const [audioInputDevices, setAudioInputDevices] = useState<MediaDevice[]>([]);
  const [videoInputDevices, setVideoInputDevices] = useState<MediaDevice[]>([]);
  const [audioStream, setAudioStream] = useState<MediaStream | null>(null);
  const [videoStream, setVideoStream] = useState<MediaStream | null>(null);

  const fetchDevices = async () => {
    try {
      const devices = await navigator.mediaDevices.enumerateDevices();
      
      setAudioInputDevices(devices.filter(device => device.kind === 'audioinput'));
      setVideoInputDevices(devices.filter(device => device.kind === 'videoinput'));
    } catch (error) {
      console.error('Error fetching devices:', error);
    }
  };

  const testAudioInput = async (deviceId?: string) => {
    try {
      if (audioStream) {
        audioStream.getTracks().forEach(track => track.stop());
      }

      const constraints: MediaStreamConstraints = {
        audio: deviceId ? { deviceId: { exact: deviceId } } : true,
        video: false
      };

      const _audioStream = await navigator.mediaDevices.getUserMedia(constraints);
      setAudioStream(_audioStream);

      const audioContext = new AudioContext();
      const source = audioContext.createMediaStreamSource(_audioStream);
      const analyser = audioContext.createAnalyser();
      source.connect(analyser);

      return { _audioStream, analyser };
    } catch (error) {
      console.error('Audio input test failed:', error);
      throw error;
    }
  };

  const testVideoInput = async (deviceId?: string) => {
    try {
      if (videoStream) {
        videoStream.getTracks().forEach(track => track.stop());
      }

      const constraints: MediaStreamConstraints = {
        video: deviceId ? { deviceId: { exact: deviceId } } : true,
        audio: false
      };

      const _videoStream = await navigator.mediaDevices.getUserMedia(constraints);
      setVideoStream(_videoStream);

      return _videoStream;
    } catch (error) {
      console.error('Video input test failed:', error);
      throw error;
    }
  };

  useEffect(() => {
    fetchDevices();

    navigator.mediaDevices.addEventListener('devicechange', fetchDevices);

    return () => {
      navigator.mediaDevices.removeEventListener('devicechange', fetchDevices);
      if (audioStream) audioStream.getTracks().forEach(track => track.stop());
      if (videoStream) videoStream.getTracks().forEach(track => track.stop());
    };
  }, [audioStream, videoStream]);

  return {
    audioInputDevices,
    videoInputDevices,
    testAudioInput,
    testVideoInput,
    audioStream,
    videoStream,
  };
};
