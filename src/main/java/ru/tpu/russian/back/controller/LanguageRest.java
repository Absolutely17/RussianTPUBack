package ru.tpu.russian.back.controller;

import org.springframework.web.bind.annotation.*;
import ru.tpu.russian.back.dto.language.*;
import ru.tpu.russian.back.service.LanguageService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/language")
public class LanguageRest {

    private final LanguageService languageService;

    public LanguageRest(LanguageService languageService) {
        this.languageService = languageService;
    }

    /**
     * Получаем все текущие языки в системе
     */
    @RequestMapping(method = GET)
    public List<LanguageResponse> getAllLanguage() {
        return languageService.getAllLanguage();
    }

    /**
     * РЕСТы для админки
     */
    @RequestMapping(method = GET, path = "/admin/table")
    public List<LanguageResponseTableRow> getTable() {
        return languageService.getTable();
    }

    @RequestMapping(method = GET, path = "/admin/allAvailableForCreate")
    public List<LanguageBaseResponse> getAllAvailableForCreate() {
        return languageService.getAllAvailableForCreate();
    }

    @RequestMapping(method = POST, path = "/admin/create")
    public void addNewLanguage(@RequestBody LanguageCreateRequest request) {
        languageService.create(request);
    }
}
