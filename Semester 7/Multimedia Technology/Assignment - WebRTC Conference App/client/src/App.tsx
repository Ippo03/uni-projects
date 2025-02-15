import { Provider } from 'react-redux';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import MainPage from './pages/MainPage';
import MeetingPage from './pages/MeetingPage';
import PreMeetingSetupPage from './pages/PreMeetingSetupPage';
import { DeviceProvider } from './providers/DeviceProvider';
import { SocketProvider } from './providers/SocketProvider';
import { store } from './store/Store';
import './styles.css';

const App: React.FC = () => {
    return (
        <Provider store={store}>
            <DeviceProvider>
                <SocketProvider>
                        <Router>
                            <Routes>
                                <Route path="/" element={<MainPage />} />
                                <Route path="/setup/:meetingID" element={<PreMeetingSetupPage />} />
                                <Route path="/meeting/:meetingID" element={<MeetingPage />} />
                            </Routes>
                        </Router>
                </SocketProvider>
            </DeviceProvider>
        </Provider>
    );
};

export default App;
