import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/Login';

import TablesPage from './pages/TablesPage';
import ReservationsPage from './pages/ReservationsPage';
import ReportPage from './pages/ReportPage';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/login" />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/tables" element={<TablesPage />} />
                <Route path="/reservations" element={<ReservationsPage />} />
                <Route path="/report" element={<ReportPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
