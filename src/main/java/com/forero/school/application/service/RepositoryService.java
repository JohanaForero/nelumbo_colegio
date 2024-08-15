package com.forero.school.application.service;


import com.forero.school.domain.agregate.DataResultAgregate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RepositoryService {
    void saveNotes(Integer subjectId, MultipartFile file);

    void validateIfSubjectExists(int subjectId);

    List<DataResultAgregate> getAllSubjectAndStudents();
}
