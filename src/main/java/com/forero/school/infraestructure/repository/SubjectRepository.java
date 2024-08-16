package com.forero.school.infraestructure.repository;

import com.forero.school.infraestructure.repository.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
}
