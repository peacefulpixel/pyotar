package org.example.corp.engine.shader;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.exception.EngineInitializationException;
import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.res.ResourceManager;
import org.example.corp.engine.res.Shader;
import org.example.corp.engine.util.LoggerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShaderProgramsManager {

    private static final Logger logger = LoggerUtils.getLogger(ShaderProgramsManager.class);

    private static final Map<String, ShaderProgram> shaderPrograms = new HashMap<>();
    private static String shaderDir = "res/shaders/";
    public static final String DEFAULT_PROGRAM = "default";

    public static void setShaderDir(String path) {
        shaderDir = path;
    }

    public static ShaderProgram createShaderProgram(String name) throws ShaderInitializationException {
        ShaderProgram program;
        program = new ShaderProgram();
        Shader vs = ResourceManager.get(Shader.class, shaderDir + name + ".vs");
        Shader fs = ResourceManager.get(Shader.class, shaderDir + name + ".fs");

        if (vs == null || fs == null) {
            throw new ShaderInitializationException("Can't load one of shader program resources: "
                    + shaderDir + name + ".(vs/fs)");
        }

        program.createVertexShader(vs.getShaderSource());
        program.createFragmentShader(fs.getShaderSource());
        program.link();
        shaderPrograms.put(name, program);
        return program;
    }

    public static ShaderProgram getShaderProgram(String name) throws ShaderInitializationException {
        ShaderProgram program;
        if ((program = shaderPrograms.get(name)) == null) {
            if ((program = shaderPrograms.get(DEFAULT_PROGRAM)) == null) {
                logger.info("Initializing default shader program");
                return createShaderProgram(DEFAULT_PROGRAM);
            }
        }

        return program;
    }

    public static ShaderProgram getDefaultProgram() throws ShaderInitializationException {
        return getShaderProgram(DEFAULT_PROGRAM);
    }
}
