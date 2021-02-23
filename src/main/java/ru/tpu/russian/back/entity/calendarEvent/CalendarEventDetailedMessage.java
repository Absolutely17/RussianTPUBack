package ru.tpu.russian.back.entity.calendarEvent;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "CALENDAR_EVENT_DETAILED_MESSAGE")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventDetailedMessage {

    @Id
    @Column(name = "ID")
    private String id;

    @Setter
    @Column(name = "MESSAGE")
    private String message;

    @Setter
    @OneToOne
    @JoinColumn(name = "CALENDAR_EVENT_ID")
    private CalendarEvent calendarEvent;

    @Column(name = "LOAD_DATE")
    private Date loadDate;

    public CalendarEventDetailedMessage(String message) {
        id = UUID.randomUUID().toString();
        this.message = message;
        loadDate = new Date();
    }
}
