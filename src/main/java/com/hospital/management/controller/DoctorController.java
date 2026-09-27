package com.hospital.management.controller;

import com.hospital.management.entity.Doctor;
import com.hospital.management.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public List<Doctor> getAll() {
        return doctorService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public Doctor getById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Doctor> create(@Valid @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.create(doctor));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Doctor update(@PathVariable Long id, @Valid @RequestBody Doctor doctor) {
        return doctorService.update(id, doctor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
