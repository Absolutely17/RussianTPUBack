package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;
import ru.tpu.russian.back.dto.systemConfig.SystemParameterResponse;
import ru.tpu.russian.back.entity.SystemParameter;
import ru.tpu.russian.back.mapper.SystemConfigMapper;
import ru.tpu.russian.back.repository.systemConfig.SystemConfigRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper mapper;

    public SystemConfigService(
            SystemConfigRepository systemConfigRepository,
            SystemConfigMapper mapper
    ) {
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
        systemConfigRepository.deleteAll();
        params.forEach(it -> {
            SystemParameter param = new SystemParameter(
                    it.getName(),
                    it.getKey(),
                    it.getValue(),
                    it.getDescription(),
                    it.isDisabled(),
                    it.getType()
            );
            systemConfigRepository.save(param);
        });
    }
}
