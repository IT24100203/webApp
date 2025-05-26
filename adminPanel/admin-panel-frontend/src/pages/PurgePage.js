import { purgeOldReservations } from "../api/dashboardApi";
import Navbar from "../components/Navbar";

export default function PurgePage() {
    const handlePurge = async () => {
        if (window.confirm("Are you sure you want to purge old reservations?")) {
            await purgeOldReservations();
            alert("Old reservations purged.");
        }
    };

    return (
        <div>
            <Navbar />
            <div style={{ padding: "30px" }}>
                <h1>Purge Old Reservations</h1>
                <button className="purge-button" onClick={handlePurge}>Purge Now</button>
            </div>
        </div>
    );
}
