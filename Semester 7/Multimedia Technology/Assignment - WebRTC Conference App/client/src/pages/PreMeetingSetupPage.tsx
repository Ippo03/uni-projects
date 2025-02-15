import { VideoCameraOutlined } from '@ant-design/icons';
import { Button, Card, Checkbox, Col, Divider, Modal, Row, Select, Space, message, } from 'antd';
import { RtpCapabilities } from 'mediasoup-client/lib/RtpParameters';
import React, { useEffect, useRef, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import { useMediaDevices } from '../hooks/media/media-devices/UseMediaDevicesHook';
import { useJoinMeeting } from '../hooks/meeting-setup/JoinMeetingHook';
import { useDevice } from '../providers/DeviceProvider';
import {
  setIsCameraOn,
  setIsMuted,
  setPreferredCamera,
  setPreferredMic,
} from '../store/Reducer';

const { Option } = Select;

const PreMeetingSetupPage: React.FC = () => {
  const {
    audioInputDevices,
    videoInputDevices,
    testVideoInput,
  } = useMediaDevices();

  const { meetingID } = useParams<{ meetingID: string }>();

  const dispatch = useDispatch();
  const joinMeeting = useJoinMeeting();
  const [selectedMic, setSelectedMic] = useState<string | undefined>(undefined);
  const [selectedCamera, setSelectedCamera] = useState<string | undefined>(undefined);
  const [joinMuted, setJoinMuted] = useState<boolean>(false);
  const [joinVideo, setJoinVideo] = useState<boolean>(false);
  // const [isBackgroundBlurred, setIsBackgroundBlurred] = useState<boolean>(false);

  const videoRef = useRef<HTMLVideoElement>(null);
  const { initializeDevice } = useDevice();

  const navigate = useNavigate();

  useEffect(() => {
    // Set default selected camera if available and not already set
    if (videoInputDevices.length > 0 && !selectedCamera) {
      setSelectedCamera(videoInputDevices[0].deviceId);
    }

    // Set default selected mic if available and not already set
    if (audioInputDevices.length > 0 && !selectedMic) {
      setSelectedMic(audioInputDevices[0].deviceId);
    }
  }, [videoInputDevices, audioInputDevices, selectedCamera, selectedMic]); 

  const handleTestVideo = async () => {
    try {
      const stream = await testVideoInput(selectedCamera);
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }
    } catch (error) {
      message.error('Failed to test video input');
      console.error('Video test error:', error);
    }
  };

  const handleSavePreferences = () => {
    dispatch(setPreferredCamera(selectedCamera || videoInputDevices[0]?.deviceId || null));
    dispatch(setPreferredMic(selectedMic || audioInputDevices[0]?.deviceId || null));
    dispatch(setIsMuted(joinMuted));
    dispatch(setIsCameraOn(joinVideo));
    // dispatch(setIsCameraBlurred(isBackgroundBlurred));
  };  

  const handleJoinMeeting = async () => {
    handleSavePreferences();

    const rtpCapabilities: RtpCapabilities | undefined = await joinMeeting(meetingID!);
    if (rtpCapabilities) {
      await initializeDevice(rtpCapabilities);
      navigate(`/meeting/${meetingID}`);
    } else {
      message.error('Meeting ID is invalid or meeting has ended');
      setTimeout(() => {
        navigate('/');
      }, 1000);
      return;
    }
  }

  return (
    <Modal
    title={
      <span className="text-xl font-bold text-blue-500">
        Pre-Meeting Setup
      </span>
    }
      open={true}
      width={700}
      height={700}
      closable
      onCancel={() => {
        navigate('/');
      }}
      footer={[
        <Button key="join" type="primary" onClick={handleJoinMeeting}>
          Join Meeting
        </Button>,
      ]}
    >
      <Row gutter={16} justify="center">
        <Col span={24}>
          <Card title="Camera & Mic Test" bordered>
            <Row gutter={16}>
              <Col span={24} className="flex flex-col items-center">
                <video
                  ref={videoRef}
                  autoPlay
                  style={{
                    maxWidth: 400,
                    maxHeight: 200,
                    backgroundColor: 'black',
                    marginBottom: '1rem',
                  }}
                />
                <Button
                  icon={<VideoCameraOutlined />}
                  onClick={handleTestVideo}
                >
                  Test Camera
                </Button>
              </Col>
              <Col span={24} className="flex flex-col items-center">
                {/* Add audio visualizer to visualize active mic */}
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>

      <Divider />

      <Row gutter={16}>
        <Col span={12}>
          <Card title="Video Settings" bordered>
            <Space direction="vertical" size="middle" style={{ width: '100%' }}>
              <Select
                value={selectedCamera}
                onChange={(value) => setSelectedCamera(value)}
                placeholder="Select your camera"
                style={{ width: '100%' }}
              >
                {videoInputDevices.length > 0 ? (
                  videoInputDevices.map(device => (
                    <Option key={device.deviceId} value={device.deviceId}>
                      {device.label}
                    </Option>
                  ))
                ) : (
                  <Option disabled>No cameras found</Option>
                )}
              </Select>
              {/* <Checkbox
                checked={isBackgroundBlurred}
                onChange={(e) => setIsBackgroundBlurred(e.target.checked)}
              >
                Blur background
              </Checkbox> */}
              <Checkbox
                checked={joinVideo}
                onChange={(e) => setJoinVideo(e.target.checked)}
              >
                Disable camera
              </Checkbox>
            </Space>
          </Card>
        </Col>

        <Col span={12}>
          <Card title="Audio Settings" bordered>
            <Space direction="vertical" size="middle" style={{ width: '100%' }}>
              <Select
                value={selectedMic}
                onChange={(value) => setSelectedMic(value)}
                placeholder="Select your microphone"
                style={{ width: '100%' }}
              >
                {audioInputDevices.length > 0 ? (
                  audioInputDevices.map(device => (
                    <Option key={device.deviceId} value={device.deviceId}>
                      {device.label}
                    </Option>
                  ))
                ) : (
                  <Option disabled>No microphones found</Option>
                )}
              </Select>

              <Checkbox
                checked={joinMuted}
                onChange={(e) => setJoinMuted(e.target.checked)}
              >
                Join Muted
              </Checkbox>
            </Space>
          </Card>
        </Col>
      </Row>
    </Modal>
  );
};

export default PreMeetingSetupPage;