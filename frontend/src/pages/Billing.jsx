import { useEffect, useState } from "react";
import Layout from "../components/Layout";
import { getBills, createBill, markBillPaid, deleteBill } from "../api/billing";
import { getPatients } from "../api/patients";
import { useAuth } from "../auth/AuthContext";

export default function Billing() {
  const [bills, setBills] = useState([]);
  const [patients, setPatients] = useState([]);
  const [form, setForm] = useState({ patientId: "", amount: "" });
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const isAdmin = user.role === "ROLE_ADMIN";

  useEffect(() => {
    load();
    getPatients().then(setPatients);
  }, []);

  function load() {
    getBills().then(setBills).catch(() => setError("Failed to load billing records"));
  }

  async function handleCreate(e) {
    e.preventDefault();
    setError(null);
    try {
      await createBill(form.patientId, null, { amount: form.amount });
      setForm({ patientId: "", amount: "" });
      load();
    } catch {
      setError("Failed to create bill");
    }
  }

  async function handlePay(id) {
    await markBillPaid(id);
    load();
  }

  async function handleDelete(id) {
    if (!confirm("Delete this bill?")) return;
    await deleteBill(id);
    load();
  }

  return (
    <Layout>
      <div className="p-6 max-w-4xl mx-auto">
        <h1 className="text-xl font-bold mb-4">Billing</h1>
        {error && <p className="text-red-600 text-sm mb-2">{error}</p>}

        <form onSubmit={handleCreate} className="grid grid-cols-3 gap-2 mb-6 bg-white p-4 rounded shadow">
          <select className="border rounded px-2 py-1" value={form.patientId}
                  onChange={(e) => setForm({ ...form, patientId: e.target.value })} required>
            <option value="">Select patient</option>
            {patients.map((p) => <option key={p.id} value={p.id}>{p.name}</option>)}
          </select>
          <input className="border rounded px-2 py-1" type="number" step="0.01" placeholder="Amount"
                 value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} required />
          <button type="submit" className="bg-slate-800 text-white rounded py-2 hover:bg-slate-700">
            Create Bill
          </button>
        </form>

        <table className="w-full bg-white rounded shadow overflow-hidden">
          <thead className="bg-slate-100 text-left text-sm">
            <tr><th className="p-2">Patient</th><th className="p-2">Amount</th><th className="p-2">Status</th><th className="p-2">Date</th><th className="p-2"></th></tr>
          </thead>
          <tbody>
            {bills.map((b) => (
              <tr key={b.id} className="border-t text-sm">
                <td className="p-2">{b.patient?.name}</td>
                <td className="p-2">${Number(b.amount).toFixed(2)}</td>
                <td className="p-2">
                  <span className={b.status === "PAID" ? "text-green-600" : "text-amber-600"}>{b.status}</span>
                </td>
                <td className="p-2">{b.billingDate?.split("T")[0]}</td>
                <td className="p-2 space-x-2">
                  {b.status !== "PAID" && (
                    <button onClick={() => handlePay(b.id)} className="text-green-700 hover:underline">Mark Paid</button>
                  )}
                  {isAdmin && (
                    <button onClick={() => handleDelete(b.id)} className="text-red-600 hover:underline">Delete</button>
                  )}
                </td>
              </tr>
            ))}
            {bills.length === 0 && (
              <tr><td colSpan={5} className="p-4 text-center text-slate-400">No billing records yet</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </Layout>
  );
}
