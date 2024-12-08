package com.forero.school.application.usecase;

import com.forero.school.application.exception.RegisterUseCaseException;
import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.GeneralAggregate;
import com.forero.school.domain.exception.CodeException;
import com.forero.school.domain.model.Registered;
import com.forero.school.infraestructure.repository.entity.StudentEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public record RegisterUseCase(RepositoryService repositoryService) {
    public void registerNotes(final Integer subjectId, final List<MultipartFile> file) {
        this.repositoryService.validateIfSubjectExists(subjectId);
        final MultipartFile[] fileArray = this.validateFiles(file);
        this.repositoryService.uploadGrades(fileArray, subjectId);
    }

    private MultipartFile[] validateFiles(final List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new RegisterUseCaseException(CodeException.INVALID_PARAMETERS, null,
                    "you must provide at least one file");
        }

        if (files.size() > 1) {
            throw new RegisterUseCaseException(CodeException.INVALID_PARAMETERS, null,
                    "only one file is allowed to be uploaded at a time");
        }
        return files.toArray(new MultipartFile[0]);
    }

    public List<GeneralAggregate<StudentEntity>> getAllSubjects() {
        return this.repositoryService.getAllSubjectAndStudents();
    }

    public List<Registered> getAllRecords(final int subjectId) {
        this.repositoryService.validateIfSubjectExists(subjectId);
        return this.repositoryService.getAllRegistered(subjectId);
    }

    public byte[] generatePDF(final int subjectId) {
        this.repositoryService.validateIfSubjectExists(subjectId);
        return this.repositoryService.generatePdf(subjectId);
    }
}