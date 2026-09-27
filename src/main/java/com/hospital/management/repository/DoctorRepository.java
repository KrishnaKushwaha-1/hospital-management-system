package com.hospital.management.repository;

import com.hospital.management.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserUsername(String username);
}
