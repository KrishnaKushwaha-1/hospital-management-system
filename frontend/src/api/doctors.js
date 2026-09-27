import api from "./client";

export const getDoctors = () => api.get("/doctors").then((r) => r.data);
export const getDoctor = (id) => api.get(`/doctors/${id}`).then((r) => r.data);
export const createDoctor = (data) => api.post("/doctors", data).then((r) => r.data);
export const updateDoctor = (id, data) => api.put(`/doctors/${id}`, data).then((r) => r.data);
export const deleteDoctor = (id) => api.delete(`/doctors/${id}`);
