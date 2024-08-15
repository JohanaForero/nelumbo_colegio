package com.forero.school.application.usecase;

import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.DataResultAgregate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public record RegisterUseCase(RepositoryService repositoryService) {
    public void registerNotes(final Integer subjectId, final MultipartFile file) {
        this.repositoryService.validateIfSubjectExists(subjectId);
        this.repositoryService.saveNotes(subjectId, file);
    }

    public List<DataResultAgregate> getAllSubjects() {
        final List<DataResultAgregate> subjects = this.repositoryService.getAllSubjectAndStudents();
        return subjects;
    }
}
