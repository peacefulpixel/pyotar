package org.example.corp.engine.entity;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.graphics.VertexArray;

import static org.example.corp.engine.shader.ShaderProgram.ATTR_BOUNDS;

public abstract class SquareEntity extends RenderableEntity {

    public SquareEntity(Sprite sprite) throws EngineException {
        super(sprite);
    }

    protected abstract void vertexArrayPostRefreshing();

    @Override
    protected void refreshVertexArray() {
        Sprite sprite = getSprite();

        float axisX  = sprite.getAxisX();
        float axisY  = sprite.getAxisY();
        float width  = sprite.getWidth();
        float height = sprite.getHeight();

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
        Camera camera = Window.MAIN_WINDOW.getCamera();

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
