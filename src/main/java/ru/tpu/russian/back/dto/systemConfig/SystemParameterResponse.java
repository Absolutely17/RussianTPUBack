package ru.tpu.russian.back.dto.systemConfig;

import lombok.*;

/**
 * Ответ в АПИ на получение системного параметра
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SystemParameterResponse {

    private String id;

    private String key;

    private String value;

    private String name;

    private String description;

    private boolean disabled;

    private String type;
}
