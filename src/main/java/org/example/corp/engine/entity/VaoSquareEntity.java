package org.example.corp.engine.entity;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.graphics.VaoVertexArray;
import org.example.corp.engine.graphics.VertexArray;
import org.example.corp.engine.util.GLUtils;

import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_BOUNDS;
import static org.example.corp.engine.util.GLUtils.SQUARE_ELEMENTS_ARRAY;
import static org.example.corp.engine.util.GLUtils.VECTOR_SIZE_2D;

public class VaoSquareEntity extends SquareEntity {

    public VaoSquareEntity(Sprite sprite) throws EngineException {
        super(sprite);
    }

    @Override
    protected void vertexArrayPostRefreshing() {
        //TODO: Check is that might affect performance or not
        vertexArray = GLUtils.unmapVertexArrayFromElements(vertexArray, SQUARE_ELEMENTS_ARRAY, VECTOR_SIZE_2D);
    }

    @Override
    protected VertexArray createVertexArray() {
        return new VaoVertexArray(ATTR_BOUNDS, vertexArray, SQUARE_ELEMENTS_ARRAY);
    }
}
