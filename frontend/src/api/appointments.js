import api from "./client";

export const getAppointments = () => api.get("/appointments").then((r) => r.data);
export const createAppointment = (patientId, doctorId, data) =>
  api.post(`/appointments/patient/${patientId}/doctor/${doctorId}`, data).then((r) => r.data);
export const updateAppointmentStatus = (id, status) =>
  api.patch(`/appointments/${id}/status/${status}`).then((r) => r.data);
export const deleteAppointment = (id) => api.delete(`/appointments/${id}`);
