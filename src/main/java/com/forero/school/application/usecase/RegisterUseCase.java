package com.forero.school.application.usecase;

import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.DataResultAgregate;
import com.forero.school.domain.model.Registered;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public record RegisterUseCase(RepositoryService repositoryService) {
    public void registerNotes(final Integer subjectId, final MultipartFile file) throws IOException {
        this.repositoryService.validateIfSubjectExists(subjectId);
        this.repositoryService.uploadGrades(file, Long.valueOf(subjectId));
    }

    public List<DataResultAgregate> getAllSubjects() {
        final List<DataResultAgregate> subjects = this.repositoryService.getAllSubjectAndStudents();
        return subjects;
    }

    public List<Registered> getAllRecords() {
        return this.repositoryService.getAllRegistered();
    }

    public byte[] generatePDF() {
        return this.repositoryService.generatePdf();
    }
}