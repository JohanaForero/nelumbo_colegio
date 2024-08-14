package com.forero.school.infraestructure.repository;

import com.forero.school.infraestructure.repository.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
