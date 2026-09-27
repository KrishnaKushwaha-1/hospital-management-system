import api from "./client";

export const getBills = () => api.get("/billing").then((r) => r.data);
export const createBill = (patientId, appointmentId, data) => {
  const query = appointmentId ? `?appointmentId=${appointmentId}` : "";
  return api.post(`/billing/patient/${patientId}${query}`, data).then((r) => r.data);
};
export const markBillPaid = (id) => api.patch(`/billing/${id}/pay`).then((r) => r.data);
export const deleteBill = (id) => api.delete(`/billing/${id}`);
