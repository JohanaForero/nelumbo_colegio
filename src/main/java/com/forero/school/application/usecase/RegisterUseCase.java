package com.forero.school.application.usecase;

import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.GeneralAggregate;
import com.forero.school.domain.model.Registered;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public record RegisterUseCase(RepositoryService repositoryService) {
    public void registerNotes(final Integer subjectId, final MultipartFile file) {
        this.repositoryService.validateIfSubjectExists(subjectId);
        this.repositoryService.uploadGrades(file, subjectId);
    }

    public List<GeneralAggregate> getAllSubjects() {
        return this.repositoryService.getAllSubjectAndStudents();
    }

    public List<Registered> getAllRecords() {
        return this.repositoryService.getAllRegistered();
    }

    public byte[] generatePDF() {
        return this.repositoryService.generatePdf();
    }
}