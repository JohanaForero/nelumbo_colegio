package com.forero.school.infraestructure.repository;

import com.forero.school.infraestructure.repository.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByDocumentNumber(String documentNumber);
}
