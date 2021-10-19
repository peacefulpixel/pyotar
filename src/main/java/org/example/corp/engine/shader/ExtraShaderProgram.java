package org.example.corp.engine.shader;

import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.glsl.type.Float;
import org.example.corp.engine.glsl.type.*;
import org.example.corp.engine.shader.Attribute;
import org.example.corp.engine.shader.ShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class ExtraShaderProgram extends ShaderProgram {

    protected ExtraShaderProgram() throws ShaderInitializationException {
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
    }

    @Override
    protected String getName() {
        return "def";
    }
}
