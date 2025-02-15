import { Typography } from 'antd';
import React from 'react';

const ChatSection: React.FC = () => {
  return (
    <div>
      <Typography.Text strong>Group Chat</Typography.Text>
      <div className="mt-2">
        <div className="bg-blue-50 rounded-md p-2">
          <Typography.Text strong>Ippokratis:</Typography.Text>
          <Typography.Text> Hello everyone!</Typography.Text>
        </div>
        <div className="bg-blue-100 mt-2 rounded-md p-2">
          <Typography.Text strong>Kostas:</Typography.Text>
          <Typography.Text> Good afternoon!</Typography.Text>
        </div>
      </div>
    </div>
  );
};

export default ChatSection;
