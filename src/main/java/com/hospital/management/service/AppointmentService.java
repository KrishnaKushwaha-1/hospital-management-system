package com.hospital.management.service;

import com.hospital.management.entity.*;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               PatientRepository patientRepository,
                               DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public Appointment getById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    public List<Appointment> getByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> getByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public Appointment create(Long patientId, Long doctorId, Appointment appointment) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        appointment.setId(null);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }
        return appointmentRepository.save(appointment);
    }

    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment existing = getById(id);
        existing.setStatus(status);
        return appointmentRepository.save(existing);
    }

    public Appointment reschedule(Long id, Appointment updated) {
        Appointment existing = getById(id);
        existing.setAppointmentDateTime(updated.getAppointmentDateTime());
        existing.setReason(updated.getReason());
        return appointmentRepository.save(existing);
    }

    public void delete(Long id) {
        Appointment existing = getById(id);
        appointmentRepository.delete(existing);
    }
}
