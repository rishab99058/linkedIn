package com.rishab99058.linkedIn.user_service.repository;

import com.rishab99058.linkedIn.user_service.entity.UserStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatRepository extends JpaRepository<UserStat, String> {
}