import { Button, Form, Modal } from "antd";
import { MEETING_ID_LENGTH } from "../../constants";

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import InputWithCopy from "../inputs/InputWithCopy";

const JoinMeetingModal: React.FC<ModalProps> = ({ visible, onClose })=> {
    const [meetingID, setMeetingID] = useState<string>(""); 
    const navigate = useNavigate();
    
    const handleOk = () => {
        return 'Ok';
    }

    const handleCancel = () => {
        onClose();
    }

    return (
        <Modal
            title="Join an existing meeting"
            open={visible}
            onOk={handleOk}
            confirmLoading={false} 
            onCancel={handleCancel}
            footer={[
                <Button key="back" onClick={onClose}>
                    Cancel
                </Button>,
                <Button 
                    type="primary"
                    key="start" 
                    onClick={() => {
                        navigate(`/setup/${meetingID}`, { replace: true });
                    }}
                >  
                    Join
                </Button>,
            ]}
        >   
            <Form name="trigger" style={{ maxWidth: 500 }} layout="vertical" autoComplete="off" autoCapitalize="on">
                <Form.Item
                    required
                    hasFeedback
                    label="Meeting ID"
                    name="Meeting ID"
                    className="mt-6"
                    validateDebounce={500}
                    rules={[
                        {
                            type: 'string',
                            min: MEETING_ID_LENGTH,
                            max: MEETING_ID_LENGTH,
                            message: `Meeting ID must be a sequence of ${MEETING_ID_LENGTH} characters.`
                        },
                        {
                            required: true,
                            message: 'Meeting ID is required.'
                    }]}
                >
                    <InputWithCopy
                        placeholder= "Enter the meeting ID"
                        onChange={(e) => setMeetingID(e.target.value)}
                    />                
                </Form.Item>
            </Form>
        </Modal>
    );

}

interface ModalProps {
    visible: boolean;
    onClose: () => void;
}

export default JoinMeetingModal;