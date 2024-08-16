package com.forero.school.application.service;


import com.forero.school.domain.agregate.GeneralAggregate;
import com.forero.school.domain.model.Registered;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RepositoryService {

    void validateIfSubjectExists(int subjectId);

    List<GeneralAggregate> getAllSubjectAndStudents();

    List<Registered> getAllRegistered();

    void uploadGrades(MultipartFile file, int idSubject);

    byte[] generatePdf();
}
