package com.rishab99058.linkedIn.user_service.repository;

import com.rishab99058.linkedIn.user_service.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, String> {
    List<Experience> findByUserId(String userId);
}