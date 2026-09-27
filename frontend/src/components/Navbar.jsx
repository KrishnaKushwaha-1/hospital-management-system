import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  if (!user) return null;

  const role = user.role.replace("ROLE_", "");

  function handleLogout() {
    logout();
    navigate("/login");
  }

  const links = [
    { to: "/patients", label: "Patients", roles: ["ADMIN", "DOCTOR", "STAFF"] },
    { to: "/doctors", label: "Doctors", roles: ["ADMIN", "DOCTOR", "STAFF"] },
    { to: "/appointments", label: "Appointments", roles: ["ADMIN", "DOCTOR", "STAFF"] },
    { to: "/billing", label: "Billing", roles: ["ADMIN", "STAFF"] },
  ];

  return (
    <nav className="bg-slate-800 text-white px-6 py-3 flex items-center justify-between">
      <div className="flex items-center gap-6">
        <span className="font-bold">Hospital MS</span>
        {links
          .filter((l) => l.roles.includes(role))
          .map((l) => (
            <Link key={l.to} to={l.to} className="text-sm text-slate-200 hover:text-white">
              {l.label}
            </Link>
          ))}
      </div>
      <div className="flex items-center gap-4 text-sm">
        <span className="text-slate-300">{user.username} · {role}</span>
        <button onClick={handleLogout} className="bg-slate-600 px-3 py-1 rounded hover:bg-slate-500">
          Logout
        </button>
      </div>
    </nav>
  );
}
