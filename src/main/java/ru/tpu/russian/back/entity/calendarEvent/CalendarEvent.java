package ru.tpu.russian.back.entity.calendarEvent;

import lombok.*;
import ru.tpu.russian.back.enums.CalendarEventGroupTarget;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.*;

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

    @Nullable
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(mappedBy = "calendarEvent")
    @Nullable
    private CalendarEventDetailedMessage detailedMessage;

    @Column(name = "TIMESTAMP")
    private String timestamp;

    @Column(name = "GROUP_TARGET")
    @Enumerated(EnumType.STRING)
    private CalendarEventGroupTarget targetEnum;

    @Column(name = "SEND_NOTIFICATION")
    private boolean sendNotification;

    @Nullable
    @Column(name = "ONLINE_MEETING_LINK")
    private String onlineLink;

    @Column(name = "LOAD_DATE")
    private Date loadDate;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CalendarEventTargets> targets = new HashSet<>();

    public CalendarEvent(
            String title, @Nullable String description, String timestamp, CalendarEventGroupTarget targetEnum,
            boolean sendNotification, @Nullable String onlineLink
    ) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.targetEnum = targetEnum;
        this.sendNotification = sendNotification;
        this.onlineLink = onlineLink;
        loadDate = new Date();
    }

    public void addNewTargets(Set<CalendarEventTargets> newTargets) {
        targets.addAll(newTargets);
    }
}
