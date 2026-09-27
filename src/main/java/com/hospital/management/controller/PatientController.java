package com.hospital.management.controller;

import com.hospital.management.entity.Patient;
import com.hospital.management.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public List<Patient> getAll() {
        return patientService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public Patient getById(@PathVariable Long id) {
        return patientService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Patient> create(@Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.create(patient));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public Patient update(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        return patientService.update(id, patient);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
