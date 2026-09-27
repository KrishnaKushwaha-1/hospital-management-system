package com.hospital.management.service;

import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.MedicalRecord;
import com.hospital.management.entity.Patient;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.MedicalRecordRepository;
import com.hospital.management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                 PatientRepository patientRepository,
                                 DoctorRepository doctorRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<MedicalRecord> getByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public List<MedicalRecord> getByDoctor(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }

    public MedicalRecord getById(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));
    }

    public MedicalRecord create(Long patientId, Long doctorId, MedicalRecord record) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        record.setId(null);
        record.setPatient(patient);
        record.setDoctor(doctor);
        return medicalRecordRepository.save(record);
    }

    public MedicalRecord update(Long id, MedicalRecord updated) {
        MedicalRecord existing = getById(id);
        existing.setDiagnosis(updated.getDiagnosis());
        existing.setPrescription(updated.getPrescription());
        existing.setNotes(updated.getNotes());
        return medicalRecordRepository.save(existing);
    }

    public void delete(Long id) {
        MedicalRecord existing = getById(id);
        medicalRecordRepository.delete(existing);
    }
}
