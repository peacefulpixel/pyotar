package org.example.corp.engine.shader;

import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.glsl.type.*;
import org.example.corp.engine.glsl.type.Float;
import org.example.corp.engine.util.LoggerUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {

    private static final Logger logger = LoggerUtils.getLogger(ShaderProgram.class);

    @Vec2
    public static final Attribute ATTR_BOUNDS        = new Attribute("bounds", 2, GL_FLOAT);
    @Vec2
    public static final Attribute ATTR_TEXTURE_CORDS = new Attribute("texture_cords", 2, GL_FLOAT);

    public static final String UNIFORM_ORTHO           = "ortho";
    public static final String UNIFORM_CAMERA_POSITION = "camera_position";
    public static final String UNIFORM_POSITION        = "position";
    public static final String UNIFORM_TRANSFORMATION  = "transformation";
    public static final String UNIFORM_DEPTH           = "depth";
    public static final String UNIFORM_TEXTURE_COLOR   = "texture_color";
    public static final String UNIFORM_TEXTURE_2D      = "texture_2d";

    protected final int id;

    private int vertexShaderId;
    private int fragmentShaderId;

    private boolean isBound = false;

    Map<String, Integer> uniformsCache = new HashMap<>();

    protected ShaderProgram() throws ShaderInitializationException {
        id = glCreateProgram();
        if (id == 0) {
            throw new ShaderInitializationException("Unable to create shader program " + id);
        }
    }

    /**
     * Shader should be bound before this method being invoked
     */
    protected void initAttributes() {
        ATTR_BOUNDS.setId(getAttributeId(ATTR_BOUNDS.getName()));
        ATTR_TEXTURE_CORDS.setId(getAttributeId(ATTR_TEXTURE_CORDS.getName()));
    };

    protected abstract String getName();

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

        glAttachShader(id, shaderId);

        return shaderId;
    }

    protected void link() throws ShaderInitializationException {
        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == 0) {
            throw new ShaderInitializationException("Error linking Shader code: " +
                    glGetProgramInfoLog(id, 1024));
        }

//        if (vertexShaderId != 0) {
//            glDetachShader(programId, vertexShaderId);
//        }
//        if (fragmentShaderId != 0) {
//            glDetachShader(programId, fragmentShaderId);
//        }

        glValidateProgram(id);
        if (glGetProgrami(id, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(id, 1024));
        }

    }

    protected int getAttributeId(String name) {
        return ShaderProgramsManager.getAttribute(this, name);
    }

    protected void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(id, uniformName);
        if (uniformLocation < 0) {
            logger.warning("Unable to create uniform. Apparently, it's not declared in shader program or was" +
                    "removed by OpenGL shader compiler for optimization. uniformName=" + uniformName +
                    " shaderProgramId=" + id);
            return;
        }
        uniformsCache.put(uniformName, uniformLocation);
    }

    protected void setUniform(String uniform, Vector3f value) {
        performSafeUniformAction(uniform, location -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = stack.mallocFloat(3);
                value.get(fb);
                glUniform3fv(location, fb);
            }
        });
    }

    protected void setUniform(String uniform, Matrix4f value) {
        performSafeUniformAction(uniform, location -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer fb = stack.mallocFloat(16);
                value.get(fb);
                glUniformMatrix4fv(location, false, fb);
            }
        });
    }

    protected void setUniform(String uniform, float v1, float v2, float v3) {
        performSafeUniformAction(uniform, location -> glUniform3f(location, v1, v2, v3));
    }

    protected void setUniform(String uniform, float v1, float v2) {
        performSafeUniformAction(uniform, location -> glUniform2f(location, v1, v2));
    }

    protected void setUniform(String uniform, int value) {
        performSafeUniformAction(uniform, location -> glUniform1i(location, value));
    }

    protected void setUniform(String uniform, float value) {
        performSafeUniformAction(uniform, location -> glUniform1f(location, value));
    }

    @Vec2
    public void setOrtho(float x, float y) {
        setUniform(UNIFORM_ORTHO, x, y);
    }

    @Vec2
    public void setCameraPosition(float x, float y) {
        setUniform(UNIFORM_CAMERA_POSITION, x, y);
    }

    @Vec2
    public void setPosition(float x, float y) {
        setUniform(UNIFORM_POSITION, x, y);
    }

    @Mat4
    public void setTransformation(Matrix4f m) {
        setUniform(UNIFORM_TRANSFORMATION, m);
    }

    @Float
    public void setDepth(float v) {
        setUniform(UNIFORM_DEPTH, v);
    }

    @Vec3
    public void setTextureColor(float x, float y, float z) {
        setUniform(UNIFORM_TEXTURE_COLOR, x, y ,z);
    }

    @Sampler2d
    public void setTexture2d(int v) {
        setUniform(UNIFORM_TEXTURE_2D, v);
    }

    private void performSafeUniformAction(String uniform, UniformAction action) {
        Integer location = uniformsCache.get(uniform);
        if (location == null) {
            createUniform(uniform);
            location = uniformsCache.get(uniform);
            if (location == null) {
                logger.warning("Skipping action for uniform " + uniform);
                return;
            }
        }

        Integer finalLocation = location; // wtf btw
        bindAndPerform(program -> action.perform(finalLocation));
    }

    public void bindAndPerform(ShaderProgramAction action) {
        ShaderProgramsManager.bindAndPerform(this, action);
    }

    public void free() {
        if (id != 0) {
            bindAndPerform(program -> {
                glDeleteShader(vertexShaderId);
                glDeleteShader(fragmentShaderId);
                glDeleteProgram(id);
            });
        }
    }

    private interface UniformAction {
        void perform(Integer location);
    }
}
