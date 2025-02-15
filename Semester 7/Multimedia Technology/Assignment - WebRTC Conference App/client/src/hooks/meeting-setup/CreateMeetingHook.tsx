import { useCallback } from "react";
import { useSocket } from "../../providers/SocketProvider";

export const useCreateRoom = () => {
    const socket = useSocket();

    const createMeeting = useCallback(async (roomId: string): Promise<unknown> => {
        try {
            const response = await socket.emitWithAck('createRoom', roomId);
            return response;
        } catch (error) {
            console.error('Error creating room:', error);
            throw error;
        }
    }, [socket]);

    return createMeeting;
};
