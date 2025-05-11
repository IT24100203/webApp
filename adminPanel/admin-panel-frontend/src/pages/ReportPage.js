import { useState } from "react";
import { getReport } from "../api/dashboardApi";
import Navbar from "../components/Navbar";

export default function ReportPage() {
    const [date, setDate] = useState("");
    const [report, setReport] = useState(null);

    const fetchReport = async () => {
        const res = await getReport(date);
        setReport(res.data);
    };

    return (
        <div>
            <Navbar />
            <div style={{ padding: "30px" }}>
                <h1>Generate Report</h1>

                <div className="report-inputs">
                    <input
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        className="report-date"
                    />
                    <button className="report-button" onClick={fetchReport}>
                        Get Report
                    </button>
                </div>

                {report && (
                    <div style={{ marginTop: "20px" }}>
                        <p><strong>Date:</strong> <span className="numeric">{report.date}</span></p>
                        <p><strong>Total Reservations:</strong> <span className="numeric">{report.totalReservations}</span></p>
                        <p><strong>Total Tables:</strong> <span className="numeric">{report.totalTables}</span></p>
                        <p><strong>Utilization Rate:</strong> <span className="numeric">{report.utilizationRate?.toFixed(2)}%</span></p>
                    </div>
                )}
            </div>
        </div>
    );
}
