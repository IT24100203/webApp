import { useState } from 'react';
import { login } from '../api/adminApi';
import { useNavigate } from 'react-router-dom';

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            await login({ username, password });
            navigate('/tables');
        } catch (err) {
            alert('Invalid login');
        }
    };

    return (
        <div style={container}>
            <h1 style={title}>RE-ES Admin Login</h1>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                style={input}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={input}
            />
            <button onClick={handleLogin} style={button}>Login</button>
        </div>
    );
}

// --- Styles ---
const container = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '80vh',
    fontFamily: 'Arial, sans-serif',
};

const title = {
    fontSize: '32px',
    fontWeight: 'bold',
    marginBottom: '30px',
};

const input = {
    padding: '10px',
    width: '250px',
    fontSize: '16px',
    marginBottom: '15px',
    border: '1px solid black',
};

const button = {
    padding: '10px 20px',
    fontSize: '16px',
    backgroundColor: 'black',
    color: 'white',
    border: 'none',
    cursor: 'pointer',
};
