import { createContext, useContext, useState } from "react";
import api from "../api/client";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    const username = localStorage.getItem("username");
    return token ? { token, role, username } : null;
  });

  async function login(username, password) {
    const { data } = await api.post("/auth/login", { username, password });
    localStorage.setItem("token", data.token);
    localStorage.setItem("role", data.role);
    localStorage.setItem("username", data.username);
    setUser({ token: data.token, role: data.role, username: data.username });
  }

  function logout() {
    localStorage.clear();
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
