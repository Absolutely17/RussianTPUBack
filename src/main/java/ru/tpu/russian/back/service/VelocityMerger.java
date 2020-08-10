package ru.tpu.russian.back.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class VelocityMerger {

    private static final String LOG_TAG = "";

    private final VelocityEngine engine;

    public VelocityMerger() {
        engine = new VelocityEngine();
        engine.init();
    }

    public String merge(Map<String, Object> model) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            File template = new ClassPathResource("templates/registration-confirm.html").getFile();
            FileReader reader = new FileReader(template);
            engine.evaluate(new VelocityContext(model), writer, LOG_TAG, reader);
            return writer.toString();
        }
    }
}
