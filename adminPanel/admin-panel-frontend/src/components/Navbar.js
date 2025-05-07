import { useNavigate } from "react-router-dom";
import logo from "../assets/re-es-logo.jpeg";


export default function Navbar() {
    const navigate = useNavigate();


    const logout = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };


    return (
        <div style={{
            backgroundColor: "white",
            borderBottom: "1px solid #ccc",
            padding: "10px 20px",
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between"
        }}>
            <div
                style={{ cursor: "pointer", display: "flex", alignItems: "center" }}
                onClick={() => navigate("/tables")}
            >
                <img src={logo} alt="RE-ES" style={{ height: "40px", marginRight: "10px" }} />
            </div>


            <div style={{ display: "flex", gap: "20px", alignItems: "center" }}>
                <button onClick={() => navigate("/tables")}>Tables</button>
                <button onClick={() => navigate("/reservations")}>Reservations</button>
                <button onClick={() => navigate("/report")}>Report</button>
                <button
                    onClick={logout}
                    style={{ backgroundColor: "black", color: "white", padding: "5px 10px" }}
                >
                    Logout
                </button>
            </div>
        </div>
    );
}
