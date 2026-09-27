package com.hospital.management.service;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Billing;
import com.hospital.management.entity.Patient;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.BillingRepository;
import com.hospital.management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public BillingService(BillingRepository billingRepository,
                           PatientRepository patientRepository,
                           AppointmentRepository appointmentRepository) {
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<Billing> getAll() {
        return billingRepository.findAll();
    }

    public List<Billing> getByPatient(Long patientId) {
        return billingRepository.findByPatientId(patientId);
    }

    public Billing getById(Long id) {
        return billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billing record not found with id: " + id));
    }

    public Billing create(Long patientId, Long appointmentId, Billing billing) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        billing.setId(null);
        billing.setPatient(patient);

        if (appointmentId != null) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
            billing.setAppointment(appointment);
        }

        return billingRepository.save(billing);
    }

    public Billing markPaid(Long id) {
        Billing existing = getById(id);
        existing.setStatus(com.hospital.management.entity.BillingStatus.PAID);
        return billingRepository.save(existing);
    }

    public void delete(Long id) {
        Billing existing = getById(id);
        billingRepository.delete(existing);
    }
}
