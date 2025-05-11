import { useNavigate } from "react-router-dom";
import logo from "../assets/re-es-logo.jpeg";

export default function Navbar() {
    const navigate = useNavigate();

    return (
        <nav className="navbar">
            {/* Left: Logo */}
            <div onClick={() => navigate("/dashboard")} className="navbar-left">
                <img src={logo} alt="RE-ES Logo" className="logo" />
            </div>

            {/* Right: Buttons */}
            <div className="navbar-right">
                <button className="nav-button" onClick={() => navigate("/dashboard")}>Dashboard</button>
                <button className="nav-button" onClick={() => navigate("/report")}>Report</button>
                <button className="nav-button" onClick={() => navigate("/purge")}>Purge</button>
            </div>
        </nav>
    );
}
