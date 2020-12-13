package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.systemConfig.SystemParameterResponse;
import ru.tpu.russian.back.service.SystemConfigService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * РЕСТ для работы с настройками системы
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/systemConfig")
public class SystemConfigRest {

    private final SystemConfigService systemConfigService;

    public SystemConfigRest(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    @RequestMapping(method = GET, path = "/table")
    public List<SystemParameterResponse> getTableParameters() {
        return systemConfigService.getTable();
    }

    @RequestMapping(method = PUT)
    public void updateParameters(@RequestBody List<SystemParameterResponse> params) {
        systemConfigService.update(params);
    }
}
