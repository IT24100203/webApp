import { useEffect, useState } from "react";
import { getReservations, overrideReservation } from "../api/adminApi";
import Navbar from "../components/Navbar";


export default function ReservationsPage() {
    const [reservations, setReservations] = useState([]);
    const [sort, setSort] = useState(false);


    const loadData = async () => {
        const res = await getReservations(sort);
        setReservations(res.data);
    };


    const override = async (r, status) => {
        const updated = { ...r, status };
        await overrideReservation(updated);
        loadData();
    };


    useEffect(() => {
        loadData();
    }, [sort]);


    return (
        <div>
            <Navbar />
            <div className="container" style={{ padding: "30px", fontSize: "18px" }}>
                <h1 style={{ marginBottom: "20px" }}>Reservations</h1>


                <label style={{ display: "block", marginBottom: "20px" }}>
                    <input
                        type="checkbox"
                        checked={sort}
                        onChange={() => setSort(!sort)}
                        style={{ marginRight: "10px" }}
                    />
                    Sort by time
                </label>


                <table style={{ width: "100%", borderCollapse: "collapse" }}>
                    <thead>
                    <tr style={{ borderBottom: "2px solid black" }}>
                        <th style={{ textAlign: "left", paddingBottom: "10px" }}>ID</th>
                        <th style={{ textAlign: "left", paddingBottom: "10px" }}>Name</th>
                        <th style={{ textAlign: "left", paddingBottom: "10px" }}>Time</th>
                        <th style={{ textAlign: "left", paddingBottom: "10px" }}>Table</th>
                        <th style={{ textAlign: "left", paddingBottom: "10px" }}>Status</th>
                        <th style={{ textAlign: "left", paddingBottom: "10px" }}>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {reservations.map((r) => (
                        <tr key={r.id} style={{ borderBottom: "1px solid lightgray", height: "50px" }}>
                            <td>{r.id}</td>
                            <td>{r.name}</td>
                            <td>{r.date && r.time ? new Date(`${r.date}T${r.time}`).toLocaleString() : "Invalid Time"}</td>
                            <td>{r.tableId}</td>
                            <td>{r.status}</td>
                            <td>
                                <button
                                    onClick={() => override(r, "SEATED")}
                                    style={{
                                        marginRight: "10px",
                                        backgroundColor: "black",
                                        color: "white",
                                        border: "none",
                                        padding: "5px 10px",
                                        cursor: "pointer"
                                    }}
                                >
                                    Seat
                                </button>
                                <button
                                    onClick={() => override(r, "CANCELLED")}
                                    style={{
                                        backgroundColor: "black",
                                        color: "white",
                                        border: "none",
                                        padding: "5px 10px",
                                        cursor: "pointer"
                                    }}
                                >
                                    Cancel
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}


