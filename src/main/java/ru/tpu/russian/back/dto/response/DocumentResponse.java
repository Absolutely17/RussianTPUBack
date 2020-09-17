package ru.tpu.russian.back.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import ru.tpu.russian.back.entity.DocumentWithoutContent;

@Getter
public class DocumentResponse {

    @ApiModelProperty(example = "Оплата за обучение", value = "Название документа")
    private String name;

    @ApiModelProperty(example = "14.09.2020", value = "Дата загрузки документа")
    private String loadDate;

    @ApiModelProperty(example = "https://internationals.tpu.ru:8080/api/document/download?id=ID", value = "Ссылка на скачивание документа")
    private String url;

    @ApiModelProperty(example = "oplata.docx", value = "Название файла")
    private String fileName;

    public DocumentResponse(DocumentWithoutContent document) {
        name = document.getName();
        loadDate = document.getLoadDate();
        fileName = document.getFileName();
        url = document.getUrl();
    }
}
