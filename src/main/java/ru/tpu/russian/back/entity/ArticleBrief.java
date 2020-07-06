package ru.tpu.russian.back.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@NamedStoredProcedureQuery(
        name = "GetArticlesBrief",
        procedureName = "GetArticlesBrief",
        resultClasses = {ArticleBrief.class},
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Offset", type = int.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Count", type = int.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Lang", type = String.class)
        })
@Entity
public class ArticleBrief {

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Id
    @Column(name = "ID статьи")
    private String id;

    @Column(name = "Название")
    private String topic;

    @Column(name = "Краткая версия статьи")
    private String briefText;

    @Column(name = "Тематика")
    private String subject;

    @Column(name = "Время создания")
    private Date createDate;

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }


    public String getBriefText() {
        return briefText;
    }

    public String getSubject() {
        return subject;
    }

    public String getCreateDate() {
        return formatter.format(createDate);
    }
}
