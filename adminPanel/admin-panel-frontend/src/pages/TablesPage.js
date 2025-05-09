import { useEffect, useState } from "react";
import { addTable, deleteTable, getTables } from "../api/adminApi";
import Navbar from "../components/Navbar";

export default function TablesPage() {
    const [tables, setTables] = useState([]);
    const [seats, setSeats] = useState("");

    const loadTables = async () => {
        const res = await getTables();
        setTables(res.data);
    };

    const add = async () => {
        if (!seats || seats <= 0) {
            alert("Enter a valid seat count.");
            return;
        }
        await addTable({ seats: parseInt(seats) });
        setSeats("");
        loadTables();
    };

    const remove = async (id) => {
        await deleteTable(id);
        loadTables();
    };

    useEffect(() => {
        loadTables();
    }, []);

    return (
        <div>
            <Navbar />

            <div className="container" style={{ padding: "30px", fontSize: "18px" }}>
                <h1>Tables</h1>

                <div style={{ marginBottom: "20px" }}>
                    <input
                        type="number"
                        value={seats}
                        onChange={(e) => setSeats(e.target.value)}
                        placeholder="Enter number of seats"
                        style={{ marginRight: "10px", padding: "8px", fontSize: "16px" }}
                    />
                    <button onClick={add} style={{ padding: "8px 16px" }}>Add Table</button>
                </div>

                <ul style={{ listStyle: "none", padding: 0 }}>
                    {tables.map((t) => (
                        <li key={t.id} style={{ marginBottom: "12px" }}>
                            <span><strong>Table #{t.id}</strong> – Seats: {t.seats}</span>
                            <button
                                onClick={() => remove(t.id)}
                                style={{
                                    marginLeft: "15px",
                                    padding: "6px 12px",
                                    backgroundColor: "black",
                                    color: "white",
                                    border: "none",
                                    cursor: "pointer"
                                }}
                            >
                                Remove
                            </button>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
