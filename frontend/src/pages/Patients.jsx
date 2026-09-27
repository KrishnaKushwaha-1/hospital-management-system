import { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { getPatients, createPatient, deletePatient } from "../api/patients";
import { useAuth } from "../auth/AuthContext";

const empty = { name: "", dateOfBirth: "", gender: "", email: "", phone: "", address: "" };

export default function Patients() {
  const [patients, setPatients] = useState([]);
  const [form, setForm] = useState(empty);
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const canWrite = ["ROLE_ADMIN", "ROLE_STAFF"].includes(user.role);
  const canDelete = user.role === "ROLE_ADMIN";

  useEffect(() => {
    load();
  }, []);

  function load() {
    getPatients().then(setPatients).catch(() => setError("Failed to load patients"));
  }

  async function handleAdd(e) {
    e.preventDefault();
    setError(null);
    try {
      await createPatient(form);
      setForm(empty);
      load();
    } catch {
      setError("Failed to create patient — check required fields");
    }
  }

  async function handleDelete(id) {
    if (!confirm("Delete this patient?")) return;
    await deletePatient(id);
    load();
  }

  return (
    <Layout>
      <div className="p-6 max-w-5xl mx-auto">
        <h1 className="text-xl font-bold mb-4">Patients</h1>
        {error && <p className="text-red-600 text-sm mb-2">{error}</p>}

        {canWrite && (
          <form onSubmit={handleAdd} className="grid grid-cols-3 gap-2 mb-6 bg-white p-4 rounded shadow">
            <input className="border rounded px-2 py-1" placeholder="Name"
                   value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
            <input className="border rounded px-2 py-1" type="date"
                   value={form.dateOfBirth} onChange={(e) => setForm({ ...form, dateOfBirth: e.target.value })} />
            <input className="border rounded px-2 py-1" placeholder="Gender"
                   value={form.gender} onChange={(e) => setForm({ ...form, gender: e.target.value })} />
            <input className="border rounded px-2 py-1" placeholder="Email"
                   value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
            <input className="border rounded px-2 py-1" placeholder="Phone"
                   value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            <input className="border rounded px-2 py-1" placeholder="Address"
                   value={form.address} onChange={(e) => setForm({ ...form, address: e.target.value })} />
            <button type="submit" className="col-span-3 bg-slate-800 text-white rounded py-2 hover:bg-slate-700">
              Add Patient
            </button>
          </form>
        )}

        <table className="w-full bg-white rounded shadow overflow-hidden">
          <thead className="bg-slate-100 text-left text-sm">
            <tr>
              <th className="p-2">Name</th><th className="p-2">DOB</th><th className="p-2">Gender</th>
              <th className="p-2">Email</th><th className="p-2">Phone</th><th className="p-2"></th>
            </tr>
          </thead>
          <tbody>
            {patients.map((p) => (
              <tr key={p.id} className="border-t text-sm">
                <td className="p-2">{p.name}</td>
                <td className="p-2">{p.dateOfBirth || "—"}</td>
                <td className="p-2">{p.gender || "—"}</td>
                <td className="p-2">{p.email || "—"}</td>
                <td className="p-2">{p.phone || "—"}</td>
                <td className="p-2">
                  {canDelete && (
                    <button onClick={() => handleDelete(p.id)} className="text-red-600 hover:underline">
                      Delete
                    </button>
                  )}
                </td>
              </tr>
            ))}
            {patients.length === 0 && (
              <tr><td colSpan={6} className="p-4 text-center text-slate-400">No patients yet</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </Layout>
  );
}
