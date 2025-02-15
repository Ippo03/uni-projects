import { RtpCapabilities } from 'mediasoup-client/lib/RtpParameters';
import { useCallback } from 'react';
import { useDispatch } from 'react-redux';
import { useSocket } from '../../providers/SocketProvider';
import { setPeerId } from '../../store/Reducer';
import { AppDispatch } from '../../store/Store';

interface JoinRoomResponse {
  rtpCapabilities?: RtpCapabilities;
  peerId?: string;
  error?: Error;
}

export const useJoinMeeting = () => {
  const socket = useSocket();
  const dispatch = useDispatch<AppDispatch>();
  
  const joinMeeting = useCallback(
    async (roomId: string): Promise<RtpCapabilities | undefined> => {
      const response: JoinRoomResponse = await socket.emitWithAck('joinRoom', roomId);
        
      if (response.error) {
        console.error('Error joining room:', response.error);
        return;
      }

      if (!response.peerId || !response.rtpCapabilities) {
        throw new Error('Peer ID or RTP capabilities not provided');
      }

      dispatch(setPeerId(response.peerId));
      return response.rtpCapabilities;
    },
    [socket, dispatch]
  );

  return joinMeeting;
};