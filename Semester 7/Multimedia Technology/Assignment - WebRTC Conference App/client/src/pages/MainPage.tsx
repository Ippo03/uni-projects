import { Button, Col, Divider, Layout, Row, Tooltip } from 'antd';
import { useState } from 'react';
import { LogoIcon } from '../assets/icons/LogoIcon';
import CreateMeetingModal from '../components/modals/CreateMeetingModal';
import JoinMeetingModal from '../components/modals/JoinMeetingModal';
const { Content, Footer } = Layout;

enum ModalType {
    Create = 'create',
    Join = 'join'
}

const MainPage: React.FC = () => {
    const [visibleModal, setVisibleModal] = useState<ModalType | null>(null);

    const showModal = (type: ModalType) => {
        setVisibleModal(type);
    }

    const closeModal = () => {
        setVisibleModal(null);
    }

    return (
        <Layout className='min-h-screen'>
            <Content className='flex justify-center items-center'>
                <Row className='flex flex-col justify-center items-center'>
                    <Col>
                        <LogoIcon style={{ width: '450px', height: '450px' }}/>
                    </Col>
                    <Col className="flex flex-col justify-between h-full">
                        <Button type='primary' size='large' onClick={() => {showModal(ModalType.Create)}}>
                            Start an instant meeting
                        </Button>
                        <Divider>or</Divider>
                        <Button type='default' onClick={() => {showModal(ModalType.Join)}}>
                            Join an existing meeting
                        </Button>
                    </Col>
                </Row>
            </Content>
            <Footer className="text-center flex items-center justify-center">
                <span>
                    AUEB Multimedia Technology Project © {new Date().getFullYear()}
                </span>
                <Tooltip title="{Add purpose of project}">
                    <Button type="link">
                        ?
                    </Button>
                </Tooltip>
            </Footer>

            {<CreateMeetingModal visible={visibleModal === ModalType.Create} onClose={closeModal} />}
            {<JoinMeetingModal visible={visibleModal === ModalType.Join}  onClose={closeModal}/>}
        </Layout>
    );
}

export default MainPage;