package ru.tpu.russian.back.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
public class DocumentResponse {

    private String name;

    private String loadDate;

    private String url;

    private String fileName;
}
