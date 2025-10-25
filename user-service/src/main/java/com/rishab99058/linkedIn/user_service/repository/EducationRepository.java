package com.rishab99058.linkedIn.user_service.repository;

import com.rishab99058.linkedIn.user_service.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, String> {

    @Override
    List<Education> findAll();
}