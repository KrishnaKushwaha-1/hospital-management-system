package com.hospital.management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private BillingStatus status = BillingStatus.PENDING;

    private LocalDateTime billingDate = LocalDateTime.now();

    public Billing() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BillingStatus getStatus() { return status; }
    public void setStatus(BillingStatus status) { this.status = status; }
    public LocalDateTime getBillingDate() { return billingDate; }
    public void setBillingDate(LocalDateTime billingDate) { this.billingDate = billingDate; }
}
