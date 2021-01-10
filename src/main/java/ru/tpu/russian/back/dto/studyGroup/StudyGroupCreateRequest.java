package ru.tpu.russian.back.dto.studyGroup;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyGroupCreateRequest {

    private String name;

    private String internalID;
}
