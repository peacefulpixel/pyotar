package org.example.corp.engine.shader;

import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.util.LoggerUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private static final Logger logger = LoggerUtils.getLogger(ShaderProgram.class);

    private final int programId;

    private int vertexShaderId;
    private int fragmentShaderId;

    private boolean isBound = false;

    Map<String, Integer> uniforms = new HashMap<>();

    public ShaderProgram() throws ShaderInitializationException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new ShaderInitializationException("Unable to create shader program " + programId);
        }
    }

    public void createVertexShader(String shaderCode) throws ShaderInitializationException {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws ShaderInitializationException {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws ShaderInitializationException {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new ShaderInitializationException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new ShaderInitializationException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws ShaderInitializationException {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new ShaderInitializationException("Error linking Shader code: " +
                    glGetProgramInfoLog(programId, 1024));
        }

//        if (vertexShaderId != 0) {
//            glDetachShader(programId, vertexShaderId);
//        }
//        if (fragmentShaderId != 0) {
//            glDetachShader(programId, fragmentShaderId);
//        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    public int getAttribute(String name) {
        final boolean wasBound = isBound;
        if (!isBound) bind();

        int attrId = glGetAttribLocation(programId, name);

        if (!wasBound) unbind();

        return attrId;
    }

    public void bind() {
        glUseProgram(programId);
        isBound = true;
    }

    public void unbind() {
        glUseProgram(0);
        isBound = false;
    }

    public void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            logger.warning("Unable to create uniform. Apparently, it's not declared in shader program or was" +
                    "removed by OpenGL shader compiler for optimization. uniformName=" + uniformName +
                    "shaderProgramId=" + programId);
            return;
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniform, Vector3f value) {
        performSafeUniformAction(uniform, location -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = stack.mallocFloat(3);
                value.get(fb);
                glUniform3fv(location, fb);
            }
        });
    }

    public void setUniform(String uniform, Matrix4f value) {
        performSafeUniformAction(uniform, location -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = stack.mallocFloat(16);
                value.get(fb);
                glUniformMatrix4fv(location, false, fb);
            }
        });
    }

    public void setUniform(String uniform, float v1, float v2, float v3) {
        performSafeUniformAction(uniform, location -> glUniform3f(location, v1, v2, v3));
    }

    public void setUniform(String uniform, float v1, float v2) {
        performSafeUniformAction(uniform, location -> glUniform2f(location, v1, v2));
    }

    public void setUniform(String uniform, int value) {
        performSafeUniformAction(uniform, location -> glUniform1i(location, value));
    }

    public void setUniform(String uniform, float value) {
        performSafeUniformAction(uniform, location -> glUniform1f(location, value));
    }

    private void performSafeUniformAction(String uniform, UniformAction action) {
        Integer location = uniforms.get(uniform);
        if (location == null) {
            createUniform(uniform);
            location = uniforms.get(uniform);
            if (location == null) {
                logger.warning("Skipping action from uniform " + uniform);
                return;
            }
        }

        boolean wasBound = isBound;
        if (!isBound) bind();

        action.act(location);

        if (!wasBound) unbind();
    }

    public void free() {
        unbind();
        if (programId != 0) {
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);
            glDeleteProgram(programId);
        }
    }

    private interface UniformAction {
        void act(int location);
    }
}
