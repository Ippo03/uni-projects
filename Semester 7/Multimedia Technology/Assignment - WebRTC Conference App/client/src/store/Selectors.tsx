import { useSelector } from 'react-redux';
import { RootState } from './Store';

export const usePeerId = (): string | null => {
  return useSelector((state: RootState) => state.peer.peerId);
};

export const useIsMuted = (): boolean => {
  return useSelector((state: RootState) => state.peer.isMuted);
}

export const useIsCameraOn = (): boolean => {
  return useSelector((state: RootState) => state.peer.isCameraOn);
}

export const usePreferredCamera = (): string | null => {
  return useSelector((state: RootState) => state.peer.preferredCamera);
}

export const usePreferredMic = (): string | null => {
  return useSelector((state: RootState) => state.peer.preferredMic);
}

export const useIsCameraBlurred = (): boolean => {
  return useSelector((state: RootState) => state.peer.isCameraBlurred);
}
