package ru.tpu.russian.back.entity.document;

import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.tpu.russian.back.service.DocumentService.DOCUMENT_API_URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class DocumentSuperClass {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Nullable
    @Column(name = "LAST_DOWNLOAD_DATE")
    private Date lastUseDate;

    @Column(name = "LOAD_DATE")
    private Date loadDate;

    public String getUrl() {
        return DOCUMENT_API_URL + id;
    }

    public String getLoadDate() {
        return formatter.format(loadDate);
    }

    public String getLastUseDate() {
        return lastUseDate == null ? null : formatter.format(lastUseDate);
    }
}
