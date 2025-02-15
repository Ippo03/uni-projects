import { Button, Col, Result, Row } from 'antd';
import React from 'react';
import { useNavigate } from 'react-router-dom';

const PageNotFound: React.FC = () => {
    const navigate = useNavigate();

    return (
        <Row justify="center" align="middle" style={{ height: '100vh' }}>
            <Col>
                <Result
                    status="404"
                    title="404"
                    subTitle="Sorry, the page you visited does not exist."
                    extra={
                        <Button type="primary" onClick={() => navigate('/')}>
                            Back to Home
                        </Button>
                    }
                />
            </Col>
        </Row>
    );
};

export default PageNotFound;
