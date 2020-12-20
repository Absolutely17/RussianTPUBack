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
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GROUP_INTERNAL_ID")
    private String internalID;

    @Override
    public int compareTo(StudyGroup o) {
        return name.compareTo(o.name);
    }
}
