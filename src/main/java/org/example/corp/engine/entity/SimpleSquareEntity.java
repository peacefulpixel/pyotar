package org.example.corp.engine.entity;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.SimpleVertexArray;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.graphics.VertexArray;

import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_BOUNDS;

public class SimpleSquareEntity extends SquareEntity {

    public SimpleSquareEntity(Sprite sprite) throws EngineException {
        super(sprite);
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
