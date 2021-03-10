package ru.tpu.russian.back.dto.studyGroup;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyGroupCreateRequest {

    private String name;

    private String scheduleUrl;

    private String academicPlanUrl;
}
