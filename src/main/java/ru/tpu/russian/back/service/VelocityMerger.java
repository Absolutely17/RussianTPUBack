package ru.tpu.russian.back.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.tpu.russian.back.enums.Language;

import java.io.*;
import java.util.Map;

import static java.lang.String.format;

@Service
public class VelocityMerger {

    private static final String TEMPLATE_MAIL = "templates/registration-confirm-%s.html";

    private static final String LOG_TAG = "";

    private final VelocityEngine engine;

    public VelocityMerger() {
        engine = new VelocityEngine();
        engine.init();
    }

    public String merge(Map<String, Object> model, Language language) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            File template = new ClassPathResource(format(TEMPLATE_MAIL, language.toString())).getFile();
            FileReader reader = new FileReader(template);
            engine.evaluate(new VelocityContext(model), writer, LOG_TAG, reader);
            return writer.toString();
        }
    }
}
