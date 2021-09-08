package org.example.corp.engine.entity;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.graphics.VaoVertexArray;
import org.example.corp.engine.graphics.VertexArray;

import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_BOUNDS;

public class SquareEntity extends RenderableEntity {

    private static final int[] elementsArray = new int[] {
            0, 1, 2, 2, 3, 0,
    }; // Basic square

    public SquareEntity(Sprite sprite) throws EngineException {
        super(sprite);
    }

    @Override
    protected boolean shouldBeRendered() {
        float x = getX();
        float y = getY();
        Camera camera = Window.MAIN_WINDOW.getCamera();

        boolean tl = camera.isPointVisible(vertexArray[0] + x, vertexArray[1] + y);
        boolean tr = camera.isPointVisible(vertexArray[2] + x, vertexArray[3] + y);
        boolean br = camera.isPointVisible(vertexArray[4] + x, vertexArray[5] + y);
        boolean bl = camera.isPointVisible(vertexArray[6] + x, vertexArray[7] + y);
        return tl || tr || br || bl;
    }

    @Override
    protected synchronized void refreshVertexArray() {
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
                0.0f - axisX, 0.0f - axisY,  // Bottom-left
        };

        if (vertices != null) {
            vertices.setBuffer(ATTR_BOUNDS, vertexArray);
        }
    }

    @Override
    protected VertexArray createVertexArray() {
        return new VaoVertexArray(ATTR_BOUNDS, vertexArray, elementsArray);
    }
}
