import { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { getDoctors, createDoctor, deleteDoctor } from "../api/doctors";
import { useAuth } from "../auth/AuthContext";

const empty = { name: "", specialization: "", email: "", phone: "" };

export default function Doctors() {
  const [doctors, setDoctors] = useState([]);
  const [form, setForm] = useState(empty);
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const isAdmin = user.role === "ROLE_ADMIN";

  useEffect(() => { load(); }, []);

  function load() {
    getDoctors().then(setDoctors).catch(() => setError("Failed to load doctors"));
  }

  async function handleAdd(e) {
    e.preventDefault();
    setError(null);
    try {
      await createDoctor(form);
      setForm(empty);
      load();
    } catch {
      setError("Failed to create doctor");
    }
  }

  async function handleDelete(id) {
    if (!confirm("Delete this doctor?")) return;
    await deleteDoctor(id);
    load();
  }

  return (
    <Layout>
      <div className="p-6 max-w-4xl mx-auto">
        <h1 className="text-xl font-bold mb-4">Doctors</h1>
        {error && <p className="text-red-600 text-sm mb-2">{error}</p>}

        {isAdmin && (
          <form onSubmit={handleAdd} className="grid grid-cols-2 gap-2 mb-6 bg-white p-4 rounded shadow">
            <input className="border rounded px-2 py-1" placeholder="Name"
                   value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
            <input className="border rounded px-2 py-1" placeholder="Specialization"
                   value={form.specialization} onChange={(e) => setForm({ ...form, specialization: e.target.value })} />
            <input className="border rounded px-2 py-1" placeholder="Email"
                   value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
            <input className="border rounded px-2 py-1" placeholder="Phone"
                   value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            <button type="submit" className="col-span-2 bg-slate-800 text-white rounded py-2 hover:bg-slate-700">
              Add Doctor
            </button>
          </form>
        )}

        <table className="w-full bg-white rounded shadow overflow-hidden">
          <thead className="bg-slate-100 text-left text-sm">
            <tr><th className="p-2">Name</th><th className="p-2">Specialization</th><th className="p-2">Email</th><th className="p-2">Phone</th><th className="p-2"></th></tr>
          </thead>
          <tbody>
            {doctors.map((d) => (
              <tr key={d.id} className="border-t text-sm">
                <td className="p-2">{d.name}</td>
                <td className="p-2">{d.specialization || "—"}</td>
                <td className="p-2">{d.email || "—"}</td>
                <td className="p-2">{d.phone || "—"}</td>
                <td className="p-2">
                  {isAdmin && (
                    <button onClick={() => handleDelete(d.id)} className="text-red-600 hover:underline">Delete</button>
                  )}
                </td>
              </tr>
            ))}
            {doctors.length === 0 && (
              <tr><td colSpan={5} className="p-4 text-center text-slate-400">No doctors yet</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </Layout>
  );
}
