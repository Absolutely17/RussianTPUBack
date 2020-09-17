package ru.tpu.russian.back.entity;

import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.tpu.russian.back.service.DocumentService.DOCUMENT_API_URL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Document {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    @Id
    @Column(name = "ID документа")
    private String id;

    @Column(name = "Дата загрузки документа")
    private Date loadDate;

    @Column(name = "Название документа")
    private String name;

    @Column(name = "Название файла")
    private String fileName;

    public String getUrl() {
        return DOCUMENT_API_URL + id;
    }

    public String getLoadDate() {
        return formatter.format(loadDate);
    }
}
