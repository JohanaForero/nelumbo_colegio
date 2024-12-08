package com.forero.school.domain.agregate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GeneralAggregate<T> {
    private List<T> result;
    private Integer subjectId;
    private String name;
}
