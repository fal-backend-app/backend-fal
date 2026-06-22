package com.example.backendfal.repository;

import com.example.backendfal.entity.FortuneResult;
import com.example.backendfal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FortuneResultRepository extends JpaRepository<FortuneResult, Long> {
    List<FortuneResult> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<FortuneResult> findByUserOrderByCreatedAtDesc(User user);
}