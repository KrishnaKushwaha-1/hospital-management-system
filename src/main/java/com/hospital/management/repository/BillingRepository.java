package com.hospital.management.repository;

import com.hospital.management.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByPatientId(Long patientId);
}
