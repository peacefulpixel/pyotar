package org.example.corp.engine.res;

import org.example.corp.engine.FinalObject;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.exception.ResourceInitializationException;
import org.example.corp.engine.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class Shader extends Resource {
    private final FinalObject<String> shaderSource = new FinalObject<>();

    @Override
    public void load(File file) throws ResourceInitializationException {
        try {
            shaderSource.set(FileUtils.readFileAsString(file));
        } catch (IOException e) {
            throw new ResourceInitializationException("Unable to read shader source file", e);
        }
        successfullyLoaded = true;
    }

    @Override
    public void sample() {
        shaderSource.set("#version 150 core\n\nin vec2 bounds;\nin vec2 texture_cords;\nout vec2 v_texture_cords;\n" +
                "void main()\n{\nv_texture_cords = texture_cords;\ngl_Position = vec4(bounds, 0.0, 1.0);\n}");
    }

    public String getShaderSource() {
        return shaderSource.get();
    }
}
