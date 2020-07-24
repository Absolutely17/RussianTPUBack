package ru.tpu.russian.back.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import ru.tpu.russian.back.dto.enums.ProviderType;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequestServiceDto extends RegistrationRequestDto {

    @ApiModelProperty(required = true, example = "google", value = "Сервис через который происходит аутентификация")
    private ProviderType provider;
}
