package ru.tpu.russian.back.entity.notification;

import lombok.*;
import ru.tpu.russian.back.entity.user.User;

import javax.persistence.*;
import java.util.*;

import static ru.tpu.russian.back.entity.Article.formatter;

@Entity
@Table(name = "NOTIFICATION")
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ADMIN_ID")
    private String adminId;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "NOTIFICATION_USER_LINK",
            joinColumns = {@JoinColumn(name = "NOTIFICATION_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
    )
    private Set<User> users;

    @Column(name = "LOAD_DATE")
    private Date loadDate;

    public String getLoadDate() {
        return formatter.format(loadDate);
    }

    public Notification() {
        id = UUID.randomUUID().toString();
        loadDate = new Date();
    }
}
