package com.forero.school.application.usecase;

import com.forero.school.application.service.RepositoryService;
import org.springframework.stereotype.Service;

@Service
public record RegisterUseCase(RepositoryService repositoryService) {
}
