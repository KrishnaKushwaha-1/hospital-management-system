package com.hospital.management.controller;

import com.hospital.management.entity.MedicalRecord;
import com.hospital.management.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<MedicalRecord> getByPatient(@PathVariable Long patientId) {
        return medicalRecordService.getByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<MedicalRecord> getByDoctor(@PathVariable Long doctorId) {
        return medicalRecordService.getByDoctor(doctorId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public MedicalRecord getById(@PathVariable Long id) {
        return medicalRecordService.getById(id);
    }

    // Only doctors (and admin) can write medical records
    @PostMapping("/patient/{patientId}/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<MedicalRecord> create(@PathVariable Long patientId,
                                                 @PathVariable Long doctorId,
                                                 @Valid @RequestBody MedicalRecord record) {
        return ResponseEntity.ok(medicalRecordService.create(patientId, doctorId, record));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public MedicalRecord update(@PathVariable Long id, @Valid @RequestBody MedicalRecord record) {
        return medicalRecordService.update(id, record);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
