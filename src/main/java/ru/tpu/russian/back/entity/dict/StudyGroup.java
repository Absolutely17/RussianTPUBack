package ru.tpu.russian.back.entity.dict;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_GROUP")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroup implements Comparable<StudyGroup> {

    @Id
    @Column(name = "ID")
    private String idGroup;

    @Column(name = "NAME")
    private String groupName;

    @Column(name = "GROUP_INTERNAL_ID")
    private String internalGroupID;

    @Override
    public int compareTo(StudyGroup o) {
        return groupName.compareTo(o.groupName);
    }
}
