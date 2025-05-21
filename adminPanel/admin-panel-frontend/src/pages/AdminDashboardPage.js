// AdminDashboardPage.js
import { useEffect, useState } from "react";
import { getReport } from "../api/dashboardApi";
import Navbar from "../components/Navbar";
import "../App.css";

export default function AdminDashboardPage() {
    const [report, setReport] = useState(null);

    useEffect(() => {
        const today = new Date().toISOString().slice(0, 10);
        getReport(today).then((res) => setReport(res.data));
    }, []);

    return (
        <div className="admin-panel">
            <Navbar />
            <div className="dashboard-wrapper">
                <h1 className="dashboard-title">Admin Panel</h1>

                {report ? (
                    <div className="cards">
                        <div className="card">
                            <p className="label">Date</p>
                            <p className="value numeric">{report.date}</p>
                        </div>
                        <div className="card">
                            <p className="label">Total Reservations</p>
                            <p className="value numeric">{report.totalReservations}</p>
                        </div>
                        <div className="card">
                            <p className="label">Total Tables</p>
                            <p className="value numeric">{report.totalTables}</p>
                        </div>
                        <div className="card">
                            <p className="label">Utilization Rate</p>
                            <p className="value numeric">{report.utilizationRate?.toFixed(2)}%</p>
                        </div>

                        <div className="card status-breakdown">
                            <p className="label">Reservation Status</p>
                            <p>🕓 Pending: <span className="numeric">{report.pendingCount}</span></p>
                            <p>✅ Seated: <span className="numeric">{report.seatedCount}</span></p>
                            <p>❌ Cancelled: <span className="numeric">{report.cancelledCount}</span></p>
                        </div>
                    </div>
                ) : (
                    <p className="loading">Loading report...</p>
                )}
            </div>
        </div>
    );
}
