import React from 'react';
import { Input, Button, message } from 'antd';
import { CopyOutlined } from '@ant-design/icons';

interface InputWithCopyProps {
  value?: string;
  disabled?: boolean;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
}

const InputWithCopy: React.FC<InputWithCopyProps> = ({ disabled, onChange, placeholder = "Enter your text"}) => {

  const handleCopy = async () => {
    try {
      if (!placeholder) {
        return;
      }
      await navigator.clipboard.writeText(placeholder);  
      message.success('Meeting ID Copied!');
    } catch (error) {
      message.error('Failed to copy text.');
      console.error(`Error occured while trying to copy text ${error}`);
    }
  };

  return (
    <Input
      placeholder={placeholder}
      disabled={disabled}
      onChange={onChange}
      allowClear
      suffix={
        <Button
          icon={<CopyOutlined />}
          onClick={handleCopy}
          type="text"
        />
      }
    />
  );
};

export default InputWithCopy;
