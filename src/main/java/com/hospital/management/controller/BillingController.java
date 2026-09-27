package com.hospital.management.controller;

import com.hospital.management.entity.Billing;
import com.hospital.management.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<Billing> getAll() {
        return billingService.getAll();
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<Billing> getByPatient(@PathVariable Long patientId) {
        return billingService.getByPatient(patientId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public Billing getById(@PathVariable Long id) {
        return billingService.getById(id);
    }

    @PostMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Billing> create(@PathVariable Long patientId,
                                           @RequestParam(required = false) Long appointmentId,
                                           @Valid @RequestBody Billing billing) {
        return ResponseEntity.ok(billingService.create(patientId, appointmentId, billing));
    }

    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public Billing markPaid(@PathVariable Long id) {
        return billingService.markPaid(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        billingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
