package ru.tpu.russian.back.service;

import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

import static java.lang.String.format;

@Service
public class VelocityMerger {

    private static final String TEMPLATE_CONFIRMATION_MAIL = "templates/registration-confirm-%s.html";

    private static final String TEMPLATE_RESET_PASSWORD_MAIL = "templates/reset-password-%s.html";

    private final VelocityEngine engine;

    public VelocityMerger() {
        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }

    public String merge(Map<String, Object> model, String language, MailService.TypeMessages type) throws IOException {

        Template template;
        switch (type) {
            case CONFIRMATION_MESSAGE:
                template = engine.getTemplate(
                        format(TEMPLATE_CONFIRMATION_MAIL, language),
                        "UTF-8"
                );
                break;
            case RESET_PASSWORD_MESSAGE:
                template = engine.getTemplate(
                        format(TEMPLATE_RESET_PASSWORD_MAIL, language),
                        "UTF-8"
                );
                break;
            default:
                throw new IllegalArgumentException("Wrong type message");
        }
        try (StringWriter writer = new StringWriter()) {
            template.merge(new VelocityContext(model), writer);
            return writer.toString();
        }
    }
}
