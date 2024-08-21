package com.forero.school.application.service;


import com.forero.school.domain.agregate.GeneralAggregate;
import com.forero.school.domain.model.Registered;
import com.forero.school.infraestructure.repository.entity.StudentEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RepositoryService {

    void validateIfSubjectExists(int subjectId);

    List<GeneralAggregate<StudentEntity>> getAllSubjectAndStudents();

    List<Registered> getAllRegistered(int subjectId);

    void uploadGrades(MultipartFile[] file, int idSubject);

    byte[] generatePdf(int subjectId);
}
