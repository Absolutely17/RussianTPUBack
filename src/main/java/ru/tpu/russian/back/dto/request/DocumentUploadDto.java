package ru.tpu.russian.back.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadDto {

    private String adminEmail;

    private String userEmail;

    private String fileName;

    private String documentName;
}
