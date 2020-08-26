package ru.tpu.russian.back.service;

import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
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
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }

    public String merge(Map<String, Object> model, Language language) throws IOException {

        Template template = engine.getTemplate(
                format(TEMPLATE_MAIL, language.toString()),
                "UTF-8"
        );
        try (StringWriter writer = new StringWriter()) {
            template.merge(new VelocityContext(model), writer);
            return writer.toString();
        }
    }
}
