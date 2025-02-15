import { AudioMutedOutlined, AudioOutlined, PhoneOutlined, VideoCameraAddOutlined, VideoCameraOutlined } from "@ant-design/icons";
import { Row } from "antd";
import React from "react";
import MeetingControlButton from "./MeetingControlButton";

interface Props {
  isMuted: boolean;
  isVideoEnabled: boolean;
  onToggleMute: (type: "audio" | "video") => void;
  onEndCall: () => void;
}

const Controls: React.FC<Props> = ({ isMuted, isVideoEnabled, onToggleMute, onEndCall }) => (
  <Row justify="center" gutter={16}>
      <MeetingControlButton
        icon={isMuted ? <AudioMutedOutlined /> : <AudioOutlined />}
        onClick={() => onToggleMute("audio")}
      />
      <MeetingControlButton
        icon={isVideoEnabled ? <VideoCameraOutlined /> : <VideoCameraAddOutlined />}
        onClick={() => onToggleMute("video")}
      />
      <MeetingControlButton
        icon={<PhoneOutlined />}
        onClick={onEndCall}
        danger={true}
        backgroundColor="#ff4d4f"
      />
  </Row>
);

export default Controls;
