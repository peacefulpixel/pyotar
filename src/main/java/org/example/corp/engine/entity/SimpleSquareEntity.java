package org.example.corp.engine.entity;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.SimpleVertexArray;
import org.example.corp.engine.graphics.Texture;
import org.example.corp.engine.graphics.VertexArray;

import static org.example.corp.engine.shader.program.DefaultShaderProgram.ATTR_BOUNDS;

public abstract class SimpleSquareEntity extends SquareEntity {

    public SimpleSquareEntity(Texture...textures) throws EngineException {
        super(textures);
        vertices.setBuffer(ATTR_BOUNDS, vertexArray);
    }

    @Override
    protected void vertexArrayPostRefreshing() {
    }

    @Override
    protected VertexArray createVertexArray() {
        return new SimpleVertexArray(6);
    }
}
