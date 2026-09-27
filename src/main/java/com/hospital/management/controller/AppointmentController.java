package com.hospital.management.controller;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.AppointmentStatus;
import com.hospital.management.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public List<Appointment> getAll() {
        return appointmentService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public Appointment getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public List<Appointment> getByDoctor(@PathVariable Long doctorId) {
        return appointmentService.getByDoctor(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','STAFF')")
    public List<Appointment> getByPatient(@PathVariable Long patientId) {
        return appointmentService.getByPatient(patientId);
    }

    @PostMapping("/patient/{patientId}/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Appointment> create(@PathVariable Long patientId,
                                               @PathVariable Long doctorId,
                                               @Valid @RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.create(patientId, doctorId, appointment));
    }

    @PatchMapping("/{id}/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','DOCTOR')")
    public Appointment updateStatus(@PathVariable Long id, @PathVariable AppointmentStatus status) {
        return appointmentService.updateStatus(id, status);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public Appointment reschedule(@PathVariable Long id, @Valid @RequestBody Appointment appointment) {
        return appointmentService.reschedule(id, appointment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
