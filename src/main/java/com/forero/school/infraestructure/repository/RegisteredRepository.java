package com.forero.school.infraestructure.repository;

import com.forero.school.infraestructure.repository.entity.RegisteredEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisteredRepository extends JpaRepository<RegisteredEntity, Long> {
    List<RegisteredEntity> findBySubjectId(int subjectId);

    List<RegisteredEntity> findAllBySubjectId(int subjectId);

    @Query("SELECT r FROM RegisteredEntity r WHERE r.subject.id = :subjectId ORDER BY r.average DESC")
    List<RegisteredEntity> findAllBySubjectIdOrderByAverageDesc(@Param("subjectId") Long subjectId);

    boolean existsByStudentDocumentNumberAndSubjectId(String documentNumber, int subjectId);
}
