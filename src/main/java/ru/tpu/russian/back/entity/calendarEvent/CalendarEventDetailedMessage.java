package ru.tpu.russian.back.entity.calendarEvent;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CALENDAR_EVENT_DETAILED_MESSAGE")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventDetailedMessage {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "ONLINE_MEETING_LINK")
    private String onlineLink;
}
