package com.forero.school.application.service;


import org.springframework.web.multipart.MultipartFile;

public interface RepositoryService {
    void saveNotes(Integer subjectId, MultipartFile file);
}
