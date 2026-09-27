package com.hospital.management.service;

import com.hospital.management.entity.Patient;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    public Patient create(Patient patient) {
        patient.setId(null);
        return patientRepository.save(patient);
    }

    public Patient update(Long id, Patient updated) {
        Patient existing = getById(id);
        existing.setName(updated.getName());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setGender(updated.getGender());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        return patientRepository.save(existing);
    }

    public void delete(Long id) {
        Patient existing = getById(id);
        patientRepository.delete(existing);
    }
}
