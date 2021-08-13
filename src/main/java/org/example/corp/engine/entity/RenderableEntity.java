package org.example.corp.engine.entity;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.shader.ShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.BufferUtils;

import static org.example.corp.engine.shader.ShaderProgramsManager.DEFAULT_PROGRAM;
import static org.lwjgl.opengl.GL30.*;

public abstract class RenderableEntity extends Entity implements Renderable {

    private static final int[] elementsArray = new int[] {
            0, 1, 2, 2, 3, 0,
    }; // Basic square

    private final int vaoId;
    private final int vboId;
    private final int eboId;

    private Sprite sprite;
    private float x = 0.0f;
    private float y = 0.0f;
    private float depth = 0.0f;

    private float[] vertexArray;

    private final int attrBounds;
    private final int attrTextureCords;

    private ShaderProgram shaderProgram = ShaderProgramsManager.getShaderProgram(DEFAULT_PROGRAM);

    public RenderableEntity(Sprite sprite) throws EngineException {
        this.sprite = sprite;

        attrBounds = shaderProgram.getAttribute("bounds");
        attrTextureCords = shaderProgram.getAttribute("texture_cords");

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        refreshVertexArray();
        glVertexAttribPointer(attrBounds, 2, GL_FLOAT, false, 0, 0);

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        BufferUtils.glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementsArray, GL_STATIC_DRAW);

        sprite.bind(vaoId);
        glBindVertexArray(0);
    }

    private synchronized void refreshVertexArray() {
        float axisX  = sprite.getAxisX();
        float axisY  = sprite.getAxisY();
        float width  = sprite.getWidth();
        float height = sprite.getHeight();

        vertexArray = new float[] {

                // Vertex X       Vertex Y
                 0.0f - axisX, height - axisY, // Top-left
                width - axisX, height - axisY, // Top-right
                width - axisX,   0.0f - axisY, // Bottom-right
                 0.0f - axisX,   0.0f - axisY, // Bottom-left
        };

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        BufferUtils.glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
    }

    @Override
    public synchronized void render() {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(attrBounds);
        glEnableVertexAttribArray(attrTextureCords);

        shaderProgram.setUniform("position", x, y);
        shaderProgram.setUniform("depth", -depth);
        shaderProgram.bindAndPerform(p -> {
            sprite.bindTexture();
            glDrawElements(GL_TRIANGLES, elementsArray.length, GL_UNSIGNED_INT, 0);
        });

        glDisableVertexAttribArray(attrBounds);
        glDisableVertexAttribArray(attrTextureCords);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        super.destroy();
        sprite.destroy();
        glDeleteBuffers(new int[] {vboId, eboId});
        glDeleteVertexArrays(vaoId);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public synchronized void setSprite(Sprite sprite) {
        this.sprite.destroy();
        this.sprite = sprite;
        refreshVertexArray();
    }
}
