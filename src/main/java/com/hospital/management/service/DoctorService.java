package com.hospital.management.service;

import com.hospital.management.entity.Doctor;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    public Doctor getById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    public Doctor create(Doctor doctor) {
        doctor.setId(null);
        return doctorRepository.save(doctor);
    }

    public Doctor update(Long id, Doctor updated) {
        Doctor existing = getById(id);
        existing.setName(updated.getName());
        existing.setSpecialization(updated.getSpecialization());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        return doctorRepository.save(existing);
    }

    public void delete(Long id) {
        Doctor existing = getById(id);
        doctorRepository.delete(existing);
    }
}
