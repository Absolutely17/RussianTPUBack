package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Идентификатор учебной группы")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "GetStudyGroups",
                procedureName = "GetStudyGroups",
                resultClasses = {StudyGroup.class}
        )
})
public class StudyGroup {

    @Id
    @Column(name = "ID группы")
    private String idGroup;

    @Column(name = "Название группы")
    private String groupName;

    @Column(name = "Идентификатор группы")
    private String internalGroupID;
}
