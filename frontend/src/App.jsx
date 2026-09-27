import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./auth/AuthContext";
import ProtectedRoute from "./auth/ProtectedRoute";
import Login from "./pages/Login";
import Patients from "./pages/Patients";
import Doctors from "./pages/Doctors";
import Appointments from "./pages/Appointments";
import Billing from "./pages/Billing";

function Home() {
  const { user } = useAuth();
  return <Navigate to={user ? "/patients" : "/login"} replace />;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />

          <Route path="/patients" element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN", "ROLE_STAFF", "ROLE_DOCTOR"]}>
              <Patients />
            </ProtectedRoute>
          } />

          <Route path="/doctors" element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN", "ROLE_STAFF", "ROLE_DOCTOR"]}>
              <Doctors />
            </ProtectedRoute>
          } />

          <Route path="/appointments" element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN", "ROLE_STAFF", "ROLE_DOCTOR"]}>
              <Appointments />
            </ProtectedRoute>
          } />

          <Route path="/billing" element={
            <ProtectedRoute allowedRoles={["ROLE_ADMIN", "ROLE_STAFF"]}>
              <Billing />
            </ProtectedRoute>
          } />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
