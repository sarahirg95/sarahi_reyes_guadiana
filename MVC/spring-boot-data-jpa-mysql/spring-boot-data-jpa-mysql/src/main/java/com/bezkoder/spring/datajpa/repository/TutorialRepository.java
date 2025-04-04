package com.bezkoder.spring.datajpa.repository;

import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.datajpa.model.Tutorial;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  
  List<Tutorial> findByPublished(boolean published);

  List<Tutorial> findByTitleContaining(String title);

  // 🔒 Bloqueo pesimista para evitar acceso concurrente
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Tutorial t WHERE t.id = :id")
  Optional<Tutorial> findByIdForUpdate(@Param("id") Long id);  
}
