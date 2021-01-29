package ru.tpu.russian.back.entity.calendarEvent;

import lombok.NoArgsConstructor;
import ru.tpu.russian.back.entity.user.*;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "CALENDAR_EVENT_TARGETS")
@NoArgsConstructor
public class CalendarEventTargets {

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private CalendarEvent event;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @Nullable
    private User user;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    @Nullable
    private StudyGroup group;

    public CalendarEventTargets(CalendarEvent event, @Nullable User user) {
        id = UUID.randomUUID().toString();
        this.event = event;
        this.user = user;
        group = null;
    }

    public CalendarEventTargets(CalendarEvent event, @Nullable StudyGroup group) {
        id = UUID.randomUUID().toString();
        this.event = event;
        user = null;
        this.group = group;
    }
}
