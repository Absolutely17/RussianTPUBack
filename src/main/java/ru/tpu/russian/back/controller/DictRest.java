package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.response.LanguageResponse;
import ru.tpu.russian.back.entity.dict.StudyGroup;
import ru.tpu.russian.back.service.DictService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/dict")
public class DictRest {

    private final DictService dictService;

    public DictRest(DictService dictService) {
        this.dictService = dictService;
    }

    @RequestMapping(method = GET, path = "/group")
    public List<StudyGroup> getAllGroups() {
        return dictService.getAllStudyGroups();
    }

    @RequestMapping(method = GET, path = "/language")
    public List<LanguageResponse> getAllLanguage() {
        return dictService.getAllLanguage();
    }
}
