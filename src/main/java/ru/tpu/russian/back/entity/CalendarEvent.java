package ru.tpu.russian.back.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.tpu.russian.back.enums.CalendarEventGroupTarget;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CALENDAR_EVENT")
@NoArgsConstructor
@Getter
public class CalendarEvent {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TIMESTAMP")
    private String timestamp;

    @Column(name = "GROUP_TARGET")
    @Enumerated(EnumType.STRING)
    private CalendarEventGroupTarget targetEnum;

    @Column(name = "SEND_NOTIFICATION")
    private boolean sendNotification;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CalendarEventTargets> targets = new HashSet<>();

    public CalendarEvent(String id, String title, String description, String timestamp, CalendarEventGroupTarget targetEnum, boolean sendNotification) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.targetEnum = targetEnum;
        this.sendNotification = sendNotification;
    }

    public void addNewTargets(Set<CalendarEventTargets> newTargets) {
        targets.addAll(newTargets);
    }
}
