package com.forero.school.application.exception;

import com.forero.school.domain.exception.CodeException;

public class RepositoryException extends CoreException {
    public RepositoryException(final CodeException codeException, final Exception exception, final String... fields) {
        super(codeException, exception, fields);
    }
}

