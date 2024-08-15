package com.forero.school.infraestructure.repository;

import com.forero.school.infraestructure.repository.entity.RegisteredEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisteredRepository extends JpaRepository<RegisteredEntity, Long> {
    List<RegisteredEntity> findBySubjectId(int subjectId);
}
