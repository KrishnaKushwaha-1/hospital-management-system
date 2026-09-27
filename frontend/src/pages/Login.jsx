import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const { login } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    try {
      await login(username, password);
      navigate("/patients");
    } catch (err) {
      setError(
        err.response?.status === 401
          ? "Invalid username or password"
          : "Login failed — is the backend running?"
      );
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50">
      <form onSubmit={handleSubmit} className="bg-white p-8 rounded shadow w-80 space-y-4">
        <h1 className="text-xl font-bold text-center">Hospital MS Login</h1>

        {error && <p className="text-red-600 text-sm">{error}</p>}

        <input
          className="w-full border rounded px-3 py-2"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          className="w-full border rounded px-3 py-2"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" className="w-full bg-slate-800 text-white rounded py-2 hover:bg-slate-700">
          Log in
        </button>

        <p className="text-xs text-slate-400 text-center">
          Default seeded account: admin / admin123
        </p>
      </form>
    </div>
  );
}
