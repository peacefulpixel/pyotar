package org.example.corp.engine.shader.program;

import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.shader.ShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;

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
