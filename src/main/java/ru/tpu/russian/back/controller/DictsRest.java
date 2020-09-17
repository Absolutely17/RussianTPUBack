package ru.tpu.russian.back.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.SpringFoxConfig;
import ru.tpu.russian.back.entity.StudyGroup;
import ru.tpu.russian.back.service.DictsService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/dicts")
@Api(tags = {SpringFoxConfig.DICTS_REST})
public class DictsRest {

    private final DictsService dictsService;

    public DictsRest(DictsService dictsService) {
        this.dictsService = dictsService;
    }

    @ApiOperation(value = "Получение списка групп")
    @RequestMapping(method = GET, path = "/group")
    public List<StudyGroup> getAllGroups() {
        return dictsService.getAllStudyGroups();
    }
}
