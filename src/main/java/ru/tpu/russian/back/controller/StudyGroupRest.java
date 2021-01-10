package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.studyGroup.StudyGroupCreateRequest;
import ru.tpu.russian.back.entity.dict.StudyGroup;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.service.StudyGroupService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/studyGroup")
public class StudyGroupRest {

    private final StudyGroupService studyGroupService;

    public StudyGroupRest(StudyGroupService studyGroupService) {
        this.studyGroupService = studyGroupService;
    }

    @RequestMapping(method = GET)
    public List<StudyGroup> getAllGroups() {
        return studyGroupService.getAllStudyGroups();
    }

    @RequestMapping(method = GET, path = "/{id}")
    public StudyGroup getById(@PathVariable String id) {
        return studyGroupService.getById(id);
    }

    @RequestMapping(method = PUT, path = "/{id}")
    public void editGroup(@PathVariable String id, @RequestBody StudyGroupCreateRequest editDto) throws BusinessException {
        studyGroupService.editById(id, editDto);
    }

    @RequestMapping(method = POST, path = "/admin/create")
    public void createGroup(@RequestBody StudyGroupCreateRequest createDto) {
        studyGroupService.create(createDto);
    }
}
