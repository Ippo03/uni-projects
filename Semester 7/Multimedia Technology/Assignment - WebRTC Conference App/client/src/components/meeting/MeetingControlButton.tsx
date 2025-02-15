import { Button, Col } from 'antd';
import { ButtonProps } from 'antd/lib/button';
import React from 'react';

interface MeetingControlButtonProps extends ButtonProps {
  icon: React.ReactNode;
  onClick: () => void;
  backgroundColor?: string;
}

const MeetingControlButton: React.FC<MeetingControlButtonProps> = ({
  icon,
  onClick,
  backgroundColor = '#888888',
  danger = false,
  ...props
}) => (
  <Col>
    <Button
      type="primary"
      shape="circle"
      size="large"
      icon={icon}
      style={{
        backgroundColor: backgroundColor,
        borderColor: danger ? backgroundColor : undefined,
      }}
      onClick={onClick}
      danger={danger}
      {...props}
    />
  </Col>
);

export default MeetingControlButton;
