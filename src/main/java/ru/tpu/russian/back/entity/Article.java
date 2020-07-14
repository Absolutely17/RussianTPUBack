package ru.tpu.russian.back.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "GetArticlesBriefFromMenu",
                procedureName = "GetArticlesBriefFromMenu",
                resultClasses = {Article.class},
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "menu_id", type = String.class)
                }),
        @NamedStoredProcedureQuery(
                name = "GetArticlesBrief",
                procedureName = "GetArticlesBrief",
                resultClasses = {Article.class},
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "str_id", type = String.class)
                })
})

@Entity
@Table(name = "Статья")
@Getter
@Setter
public class Article {

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Id
    @Column(name = "ID статьи")
    private String id;

    @Column(name = "Название")
    private String topic;

    @Column(name = "Текст")
    private String text;

    @Nullable
    @Column(name = "Краткая версия статьи")
    private String briefText;

    @Column(name = "Тематика")
    private String subject;

    @Column(name = "Время создания")
    private Date createDate;

    @Column(name = "Картинка статьи")
    @Nullable
    private String articleImage;

    public String getCreateDate() {
        return formatter.format(createDate);
    }
}
