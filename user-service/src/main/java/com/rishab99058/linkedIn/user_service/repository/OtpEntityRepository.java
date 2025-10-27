package com.rishab99058.linkedIn.user_service.repository;

import com.rishab99058.linkedIn.user_service.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpEntityRepository extends JpaRepository<OtpEntity, String> {
    OtpEntity findByEmailAndDeletedAtNullAndValidTrue(String email);
}