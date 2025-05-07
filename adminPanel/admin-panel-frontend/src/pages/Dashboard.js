import { useEffect, useState } from 'react';
import { getTables, getReservations, getReport } from '../api/adminApi';

export default function Dashboard() {
    const [tables, setTables] = useState([]);
    const [reservations, setReservations] = useState([]);
    const [report, setReport] = useState({});

    useEffect(() => {
        getTables().then(r => setTables(r.data));
        getReservations(false).then(r => setReservations(r.data));
        getReport(new Date().toISOString().slice(0, 10))
            .then(r => setReport(r.data));
    }, []);

    const pendingCount = reservations.filter(r => r.status === 'PENDING').length;

    return (
        <div className="container" style={{ maxWidth: '900px', margin: 'auto', paddingTop: '2rem' }}>
            <h1>Dashboard</h1>

            <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap', marginTop: '2rem' }}>
                <div style={cardStyle}>
                    <h3>Total Tables</h3>
                    <p style={valueStyle}>{tables.length}</p>
                </div>
                <div style={cardStyle}>
                    <h3>Pending Reservations</h3>
                    <p style={valueStyle}>{pendingCount}</p>
                </div>
                <div style={cardStyle}>
                    <h3>Utilization (%)</h3>
                    <p style={valueStyle}>
                        {report.utilizationRate !== undefined
                            ? report.utilizationRate.toFixed(1)
                            : 'N/A'}
                    </p>
                </div>
            </div>
        </div>
    );
}

const cardStyle = {
    flex: '1',
    minWidth: '250px',
    padding: '20px',
    border: '1px solid #ccc',
    borderRadius: '10px',
    backgroundColor: 'white',
    textAlign: 'center'
};

const valueStyle = {
    fontSize: '28px',
    fontWeight: 'bold',
    marginTop: '10px'
};
