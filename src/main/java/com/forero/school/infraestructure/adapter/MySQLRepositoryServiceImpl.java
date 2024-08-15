package com.forero.school.infraestructure.adapter;

import com.forero.school.application.service.RepositoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MySQLRepositoryServiceImpl implements RepositoryService {
    @Override
    public void saveNotes(@NonNull final Integer subjectId, @NonNull final MultipartFile file) {

    }
}

