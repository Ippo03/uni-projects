import { Typography } from 'antd';
import React from 'react';

const VideoSettingsSection: React.FC = () => {
  return (
    <div>
      <Typography.Text strong>Video Settings</Typography.Text>
      <p>Camera: Enabled</p>
      <Typography.Text strong>Audio Settings</Typography.Text>
      <p>Mic: Muted</p>
    </div>
  );
};

export default VideoSettingsSection;
