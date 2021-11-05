package org.example.corp.engine.entity;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;
import org.example.corp.engine.graphics.VertexArray;

import static org.example.corp.engine.shader.ShaderProgram.ATTR_BOUNDS;

public abstract class SquareEntity extends RenderableEntity {

    public SquareEntity(Texture ...textures) throws EngineException {
        super(textures);
    }

    protected abstract void vertexArrayPostRefreshing();

    @Override
    protected void refreshVertexArray() {

        float axisX  = getAxisX();
        float axisY  = getAxisY();
        float width  = getWidth();
        float height = getHeight();

        vertexArray = new float[]{

                // Vertex X       Vertex Y
                0.0f - axisX, height - axisY,   // Top-left
                width - axisX, height - axisY, // Top-right
                width - axisX, 0.0f - axisY,  // Bottom-right
                width - axisX, 0.0f - axisY,   // Bottom-right
                0.0f - axisX, 0.0f - axisY,    // Bottom-left
                0.0f - axisX, height - axisY,  // Top-left
        };

        vertexArrayPostRefreshing();

        if (vertices != null) {
            vertices.setBuffer(ATTR_BOUNDS, vertexArray);
        }
    }

    @Override
    protected boolean shouldBeRendered() {
        float x = getX();
        float y = getY();
        Camera camera = layer.getCamera();

        boolean tl = camera.isPointVisible(vertexArray[0] + x, vertexArray[1] + y);
        boolean tr = camera.isPointVisible(vertexArray[2] + x, vertexArray[3] + y);
        boolean br = camera.isPointVisible(vertexArray[4] + x, vertexArray[5] + y);
        boolean bl = vertices.isSupportElements() ?
                camera.isPointVisible(vertexArray[6] + x, vertexArray[7] + y):
                camera.isPointVisible(vertexArray[8] + x, vertexArray[9] + y);
        return tl || tr || br || bl;
    }

    @Override
    protected VertexArray createVertexArray() {
        return null;
    }
}
