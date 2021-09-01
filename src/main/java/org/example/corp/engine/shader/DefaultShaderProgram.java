package org.example.corp.engine.shader;

import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.glsl.type.*;
import org.example.corp.engine.glsl.type.Float;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class DefaultShaderProgram extends ShaderProgram {

    @Vec2 public static final Attribute ATTR_BOUNDS        = new Attribute("bounds", 2, GL_FLOAT);
    @Vec2 public static final Attribute ATTR_TEXTURE_CORDS = new Attribute("texture_cords", 2, GL_FLOAT);

    public static final String UNIFORM_POSITION        = "position";
    public static final String UNIFORM_TRANSFORMATION  = "transformation";
    public static final String UNIFORM_DEPTH           = "depth";
    public static final String UNIFORM_TEXTURE_COLOR   = "texture_color";
    public static final String UNIFORM_TEXTURE_2D      = "texture_2d";

    protected DefaultShaderProgram() throws ShaderInitializationException {
    }

    @Override
    protected void initAttributes() {
        ATTR_BOUNDS.setId(getAttributeId(ATTR_BOUNDS.getName()));
        ATTR_TEXTURE_CORDS.setId(getAttributeId(ATTR_TEXTURE_CORDS.getName()));
    }

    @Override
    protected String getName() {
        return ShaderProgramsManager.DEFAULT_PROGRAM;
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
}
