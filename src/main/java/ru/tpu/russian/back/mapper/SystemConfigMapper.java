package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.systemConfig.SystemParameterResponse;
import ru.tpu.russian.back.entity.SystemParameter;

/**
 * Маппер для системных параметров
 */
@Component
public class SystemConfigMapper {

    public SystemParameterResponse convertToResponse(SystemParameter parameter) {
        return new SystemParameterResponse(
                parameter.getId(),
                parameter.getKey(),
                parameter.getValue(),
                parameter.getName(),
                parameter.getDescription()
        );
    }
}
