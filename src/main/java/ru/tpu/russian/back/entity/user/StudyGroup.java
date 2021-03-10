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

    @Column(name = "SCHEDULE_URL")
    private String scheduleUrl;

    @Column(name = "ACADEMIC_PLAN_URL")
    private String academicPlanUrl;

    public StudyGroup() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public int compareTo(StudyGroup o) {
        return name.compareTo(o.name);
    }
}
