import { AudioOutlined, VideoCameraOutlined } from '@ant-design/icons';
import { List, Typography } from 'antd';
import React from 'react';

const AttendeesSection: React.FC = () => {
  const attendees = ['Ippokratis', 'Kostas', 'Dion'];

  return (
    <div>
      <Typography.Text strong>Present:</Typography.Text>
      <List
        dataSource={attendees}
        renderItem={(item) => (
          <List.Item className="py-1 flex">
            <Typography.Text strong>{item}</Typography.Text>
            <span className="ml-auto flex items-center">
              <AudioOutlined className="ml-2 text-gray-400" />
              <VideoCameraOutlined className="ml-2 text-gray-400" />
            </span>
          </List.Item>
        )}
      />
    </div>
  );
};

export default AttendeesSection;
