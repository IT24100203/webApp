import { useEffect, useState } from "react";
import { getReport, purgeReservations } from "../api/adminApi";
import Navbar from "../components/Navbar";

export default function ReportPage() {
    const [date, setDate] = useState(new Date().toISOString().slice(0, 10));
    const [report, setReport] = useState(null);

    const fetchReport = async () => {
        const res = await getReport(date);
        setReport(res.data);
    };

    useEffect(() => {
        fetchReport();
    }, []);

    const handlePurge = async () => {
        if (window.confirm("Are you sure you want to purge old reservations?")) {
            await purgeReservations();
            alert("Old reservations purged.");
        }
    };

    return (
        <div>
            <Navbar />
            <div className="container" style={{ padding: "30px", fontSize: "18px" }}>
                <h1 style={{ marginBottom: "25px" }}>Utilization Report</h1>

                <div style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "25px" }}>
                    <input
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        style={{ fontSize: "16px", padding: "8px" }}
                    />
                    <button onClick={fetchReport} style={{ padding: "8px 16px" }}>Get Report</button>
                    <button onClick={handlePurge} style={{ padding: "8px 16px", backgroundColor: "black", color: "white" }}>
                        Purge Old Reservations
                    </button>
                </div>

                {report && (
                    <div style={{ lineHeight: "2" }}>
                        <div><strong>Date:</strong> {report.date}</div>
                        <div><strong>Total Reservations:</strong> {report.totalReservations}</div>
                        <div><strong>Total Tables:</strong> {report.totalTables}</div>
                        <div><strong>Utilization Rate:</strong> {report.utilizationRate?.toFixed(1)}%</div>
                    </div>
                )}
            </div>
        </div>
    );
}
