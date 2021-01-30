package ru.tpu.russian.back.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "ARTICLE")
@Getter
@NoArgsConstructor
public class Article {

    public static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Nullable
    @Column(name = "BRIEF_TEXT")
    private String briefText;

    @Column(name = "TOPIC")
    private String topic;

    @Column(name = "LANGUAGE_ID")
    private String language;

    @Column(name = "IMAGE_ID")
    @Nullable
    private String articleImage;

    @Column(name = "COUNT_VIEW")
    private Integer countView;

    @Column(name = "LOAD_DATE")
    private Date createDate;

    public String getCreateDate() {
        return formatter.format(createDate);
    }
}
