package ru.tpu.russian.back.entity.user;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "USER_GROUP")
@Getter
@Setter
@AllArgsConstructor
public class StudyGroup implements Comparable<StudyGroup> {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GROUP_INTERNAL_ID")
    private String internalID;

    public StudyGroup() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public int compareTo(StudyGroup o) {
        return name.compareTo(o.name);
    }
}
