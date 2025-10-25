package com.rishab99058.linkedIn.user_service.repository;

import com.rishab99058.linkedIn.user_service.entity.Experience;
import com.rishab99058.linkedIn.user_service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    @EntityGraph(attributePaths = {
            "experiences", "educations",
            "skills", "languages",
            "certifications", "projects", "accomplishments"
    })
    Optional<UserEntity> findByIdAndDeletedFalse(String id);


    @EntityGraph(attributePaths = {
            "experiences", "educations",
            "skills", "languages",
            "certifications", "projects", "accomplishments"
    })
    @Query("""
        SELECT DISTINCT u FROM UserEntity u 
        LEFT JOIN u.skills s 
        LEFT JOIN u.educations edu 
        LEFT JOIN u.experiences exp 
        WHERE u.deleted = false
            AND u.deactivated = false
             AND (
            LOWER(u.firstName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.lastName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.headline) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.currentPosition) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.currentCompany) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.industry) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.about) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.country) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.state) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(u.city) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(s) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(edu.school) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(edu.degree) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(exp.company) LIKE LOWER(CONCAT('%', :filter, '%')) OR
            LOWER(exp.title) LIKE LOWER(CONCAT('%', :filter, '%'))
        )
    """)
    Page<UserEntity> searchAllFlexible(String filter, Pageable pageable);


    @EntityGraph(attributePaths = {
            "experiences", "educations",
            "skills", "languages",
            "certifications", "projects", "accomplishments"
    })
    Page<UserEntity> findAllByDeletedFalseAndDeactivatedFalse(Pageable pageable);

    @Query("SELECT e FROM UserEntity u JOIN u.experiences e WHERE u.id = :userId AND u.deleted = false AND u.deactivated= false")
    List<Experience> findExperiencesByUserId(@Param("userId") String userId);

    boolean existsByIdAndDeletedFalseAndDeactivatedFalse(String id);
}