package com.forero.school.application.usecase;

import com.forero.school.application.service.RepositoryService;
import org.springframework.stereotype.Service;

@Service
public record RegisterUseCase(RepositoryService repositoryService) {
//    public void registerNotes(final Integer subjectId, final MultipartFile file) {
//        this.repositoryService.registerNotes(subjectId, file);
//    }
}
