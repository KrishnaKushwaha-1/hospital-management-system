import { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { getAppointments, createAppointment, updateAppointmentStatus, deleteAppointment } from "../api/appointments";
import { getPatients } from "../api/patients";
import { getDoctors } from "../api/doctors";
import { useAuth } from "../auth/AuthContext";

const STATUSES = ["SCHEDULED", "COMPLETED", "CANCELLED"];

export default function Appointments() {
  const [appointments, setAppointments] = useState([]);
  const [patients, setPatients] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [form, setForm] = useState({ patientId: "", doctorId: "", appointmentDateTime: "", reason: "" });
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const canSchedule = ["ROLE_ADMIN", "ROLE_STAFF"].includes(user.role);

  useEffect(() => {
    load();
    getPatients().then(setPatients);
    getDoctors().then(setDoctors);
  }, []);

  function load() {
    getAppointments().then(setAppointments).catch(() => setError("Failed to load appointments"));
  }

  async function handleCreate(e) {
    e.preventDefault();
    setError(null);
    try {
      await createAppointment(form.patientId, form.doctorId, {
        appointmentDateTime: form.appointmentDateTime,
        reason: form.reason,
      });
      setForm({ patientId: "", doctorId: "", appointmentDateTime: "", reason: "" });
      load();
    } catch {
      setError("Failed to create appointment — check patient/doctor selection and date");
    }
  }

  async function handleStatus(id, status) {
    await updateAppointmentStatus(id, status);
    load();
  }

  async function handleDelete(id) {
    if (!confirm("Delete this appointment?")) return;
    await deleteAppointment(id);
    load();
  }

  return (
    <Layout>
      <div className="p-6 max-w-5xl mx-auto">
        <h1 className="text-xl font-bold mb-4">Appointments</h1>
        {error && <p className="text-red-600 text-sm mb-2">{error}</p>}

        {canSchedule && (
          <form onSubmit={handleCreate} className="grid grid-cols-4 gap-2 mb-6 bg-white p-4 rounded shadow">
            <select className="border rounded px-2 py-1" value={form.patientId}
                    onChange={(e) => setForm({ ...form, patientId: e.target.value })} required>
              <option value="">Select patient</option>
              {patients.map((p) => <option key={p.id} value={p.id}>{p.name}</option>)}
            </select>
            <select className="border rounded px-2 py-1" value={form.doctorId}
                    onChange={(e) => setForm({ ...form, doctorId: e.target.value })} required>
              <option value="">Select doctor</option>
              {doctors.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
            </select>
            <input className="border rounded px-2 py-1" type="datetime-local"
                   value={form.appointmentDateTime}
                   onChange={(e) => setForm({ ...form, appointmentDateTime: e.target.value })} required />
            <input className="border rounded px-2 py-1" placeholder="Reason"
                   value={form.reason} onChange={(e) => setForm({ ...form, reason: e.target.value })} />
            <button type="submit" className="col-span-4 bg-slate-800 text-white rounded py-2 hover:bg-slate-700">
              Book Appointment
            </button>
          </form>
        )}

        <table className="w-full bg-white rounded shadow overflow-hidden">
          <thead className="bg-slate-100 text-left text-sm">
            <tr>
              <th className="p-2">Patient</th><th className="p-2">Doctor</th><th className="p-2">When</th>
              <th className="p-2">Reason</th><th className="p-2">Status</th><th className="p-2"></th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((a) => (
              <tr key={a.id} className="border-t text-sm">
                <td className="p-2">{a.patient?.name}</td>
                <td className="p-2">{a.doctor?.name}</td>
                <td className="p-2">{a.appointmentDateTime?.replace("T", " ")}</td>
                <td className="p-2">{a.reason || "—"}</td>
                <td className="p-2">
                  <select value={a.status} onChange={(e) => handleStatus(a.id, e.target.value)}
                          className="border rounded px-1 py-0.5 text-xs">
                    {STATUSES.map((s) => <option key={s} value={s}>{s}</option>)}
                  </select>
                </td>
                <td className="p-2">
                  {canSchedule && (
                    <button onClick={() => handleDelete(a.id)} className="text-red-600 hover:underline">Delete</button>
                  )}
                </td>
              </tr>
            ))}
            {appointments.length === 0 && (
              <tr><td colSpan={6} className="p-4 text-center text-slate-400">No appointments yet</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </Layout>
  );
}
