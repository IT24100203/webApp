import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AdminDashboardPage from "./pages/AdminDashboardPage";
import ReportPage from "./pages/ReportPage";
import PurgePage from "./pages/PurgePage";
import './App.css';

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/dashboard" />} />
                <Route path="/dashboard" element={<AdminDashboardPage />} />
                <Route path="/report" element={<ReportPage />} />
                <Route path="/purge" element={<PurgePage />} />
            </Routes>
        </BrowserRouter>
    );
}
