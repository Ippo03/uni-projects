import { Modal, Typography } from 'antd';

const { Text } = Typography;

interface EndCallModalProps {
  visible: boolean;
  countdown: number;
}

const EndCallModal: React.FC<EndCallModalProps> = ({ visible, countdown }) => {
  return (
    <Modal
      title={null}
      visible={visible}
      footer={null}
      closable={false}
      centered
      bodyStyle={{ padding: 0 }} 
    >
      <div className="text-center bg-gray-100 p-10 rounded-lg">
        <Text className="text-lg font-medium text-gray-600">Ending Call</Text>
        <div className="text-2xl font-bold text-red-500 mt-5">
          {countdown} {countdown > 1 ? 'seconds' : 'second'}
        </div>
        <Text className="text-sm text-gray-400 mt-3 block">
          Please wait, you will be redirected shortly.
        </Text>
      </div>
    </Modal>
  );
};

export default EndCallModal;
