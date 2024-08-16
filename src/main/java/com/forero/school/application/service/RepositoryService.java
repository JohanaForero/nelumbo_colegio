package com.forero.school.application.service;


import com.forero.school.domain.agregate.DataResultAgregate;
import com.forero.school.domain.model.Registered;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RepositoryService {
//    void saveNotes(Integer subjectId, MultipartFile file);

    void validateIfSubjectExists(int subjectId);

    List<DataResultAgregate> getAllSubjectAndStudents();

    List<Registered> getAllRegistered();

    void uploadGrades(MultipartFile file, Long idSubject) throws IOException;

    byte[] generatePdf();
}
