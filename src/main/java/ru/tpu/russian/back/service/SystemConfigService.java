package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.systemConfig.SystemParameterResponse;
import ru.tpu.russian.back.mapper.SystemConfigMapper;
import ru.tpu.russian.back.repository.systemConfig.ISystemConfigRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SystemConfigService {

    private final ISystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper mapper;

    public SystemConfigService(ISystemConfigRepository systemConfigRepository,
                               SystemConfigMapper mapper) {
        this.systemConfigRepository = systemConfigRepository;
        this.mapper = mapper;
    }

    public List<SystemParameterResponse> getTable() {
        return systemConfigRepository.getAllSystemParameter()
                .stream()
                .map(mapper::convertToResponse)
                .collect(toList());
    }

    public void update(List<SystemParameterResponse> params) {
        systemConfigRepository.updateParameters(params);
    }
}
