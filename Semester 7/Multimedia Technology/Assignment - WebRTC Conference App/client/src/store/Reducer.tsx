import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface PeerState {
  peerId: string | null;
  isMuted: boolean;
  isCameraOn: boolean;
  preferredCamera: string | null;
  preferredMic: string | null;
  isCameraBlurred: boolean;
}

const initialState: PeerState = {
  peerId: null,
  isMuted: false,
  isCameraOn: false,
  preferredCamera: null,
  preferredMic: null,
  isCameraBlurred: false,
};

const peerSlice = createSlice({
  name: 'peer',
  initialState,
  reducers: {
    setPeerId: (state, action: PayloadAction<string | null>) => {
      state.peerId = action.payload;
    },
    setIsMuted: (state, action: PayloadAction<boolean>) => {
      state.isMuted = action.payload;
    },
    setIsCameraOn: (state, action: PayloadAction<boolean>) => {
      state.isCameraOn = action.payload;
    },
    setPreferredCamera: (state, action: PayloadAction<string | null>) => {
      state.preferredCamera = action.payload;
    },
    setPreferredMic: (state, action: PayloadAction<string | null>) => {
      state.preferredMic = action.payload;
    },
    setIsCameraBlurred: (state, action: PayloadAction<boolean>) => {
      state.isCameraBlurred = action.payload;
    },
  },
});

export const {
  setPeerId,
  setIsMuted,
  setIsCameraOn,
  setPreferredCamera,
  setPreferredMic,
  setIsCameraBlurred,
} = peerSlice.actions;

export default peerSlice.reducer;
