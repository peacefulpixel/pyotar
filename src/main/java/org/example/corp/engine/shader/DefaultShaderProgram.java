package org.example.corp.engine.shader;

import org.example.corp.engine.exception.ShaderInitializationException;

public class DefaultShaderProgram extends ShaderProgram {

    protected DefaultShaderProgram() throws ShaderInitializationException {
        super();
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
    }

    @Override
    protected String getName() {
        return ShaderProgramsManager.DEFAULT_PROGRAM;
    }
}
