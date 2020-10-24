package ru.tpu.russian.back.entity.dict;

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
public class StudyGroup implements Comparable<StudyGroup> {

    @Id
    @Column(name = "ID группы")
    private String idGroup;

    @Column(name = "Номер группы")
    private String groupName;

    @Column(name = "Идентификатор группы")
    private String internalGroupID;

    @Override
    public int compareTo(StudyGroup o) {
        return groupName.compareTo(o.groupName);
    }
}
