package org.example.corp.engine.shader;

import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.res.ResourceManager;
import org.example.corp.engine.res.Shader;
import org.example.corp.engine.util.LoggerUtils;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class ShaderProgramsManager {

    private static final Logger logger = LoggerUtils.getLogger(ShaderProgramsManager.class);

    private static final Map<Class<? extends ShaderProgram>, ShaderProgram> shaderPrograms = new HashMap<>();
    private static String shaderDir = "res/shaders/";

    public static final String DEFAULT_PROGRAM = "default";

    private static int boundProgram = 0;

    public static void setShaderDir(String path) {
        shaderDir = path;
    }

    public static <T extends ShaderProgram> T createShaderProgram(Class<T> clazz)
            throws ShaderInitializationException {
        T program;
        try {
            program = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ShaderInitializationException("Unable to create instance of shader class " + clazz +
                    ". Verify that empty constructor exists", e);
        }

        String name = program.getName();

        Shader vs = ResourceManager.get(Shader.class, shaderDir + name + ".vs");
        Shader fs = ResourceManager.get(Shader.class, shaderDir + name + ".fs");

        if (vs == null || fs == null) {
            throw new ShaderInitializationException("Can't load one of shader program resources: "
                    + shaderDir + name + ".(vs/fs)");
        }

        program.createVertexShader(vs.getShaderSource());
        program.createFragmentShader(fs.getShaderSource());
        program.link();
        program.initAttributes();

        shaderPrograms.put(clazz, program);
        return program;
    }

    @SuppressWarnings("unchecked")
    public static <T extends ShaderProgram> T getShaderProgram(Class<T> clazz) throws ShaderInitializationException {
        T program;
        if ((program = (T) shaderPrograms.get(clazz)) == null) {
            throw new ShaderInitializationException("Shader " + clazz + " is not initialized");
        }

        return program;
    }

    private static int bind(int id) {
        int oldProgram = boundProgram;
        if (boundProgram != id && id != 0) {
            boundProgram = id;
            glUseProgram(boundProgram);
        }

        return oldProgram;
    }

    protected synchronized static void bindAndPerform(ShaderProgram program, ShaderProgramAction action) {
        int oldId = bind(program.id);
        action.perform(program);
        bind(oldId);
    }

    protected synchronized static int getAttribute(ShaderProgram program, String attribute) {
        int oldId = bind(program.id);
        int attrId = glGetAttribLocation(program.id, attribute);
        bind(oldId);
        return attrId;
    }

    public synchronized static void bindAndPerform(ShaderProgramAction action) {
        for (ShaderProgram program : shaderPrograms.values()) {
            action.perform(program);
        }
    }

    public static void free() {
        for (ShaderProgram program : shaderPrograms.values()) {
            program.free();
        }
    }
}