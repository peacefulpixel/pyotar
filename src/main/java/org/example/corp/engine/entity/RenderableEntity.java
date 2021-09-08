package org.example.corp.engine.entity;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.base.Renderable;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.*;
import org.example.corp.engine.shader.DefaultShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.BufferUtils;

import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_BOUNDS;
import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_TEXTURE_CORDS;
import static org.lwjgl.opengl.GL30.*;

public abstract class RenderableEntity extends Entity implements Renderable {

    private Sprite sprite;
    private float x = 0.0f;
    private float y = 0.0f;
    private float depth = 0.0f;

    protected float[] vertexArray;
    protected VertexArray vertices;

    private DefaultShaderProgram shaderProgram = ShaderProgramsManager.getShaderProgram(DefaultShaderProgram.class);

    public RenderableEntity(Sprite sprite) throws EngineException {
        this.sprite = sprite;

        refreshVertexArray();
        this.vertices = createVertexArray();

        sprite.bind(vertices);
    }

    protected abstract void refreshVertexArray();
    protected abstract boolean shouldBeRendered();
    protected abstract VertexArray createVertexArray();

    @Override
    public synchronized void render() {
        if (!shouldBeRendered()) return;

        vertices.bind();
        ATTR_BOUNDS.enable();
        ATTR_TEXTURE_CORDS.enable();

        shaderProgram.setPosition(x, y);
        shaderProgram.setDepth(-depth);
        sprite.setUniforms();
        shaderProgram.bindAndPerform(p -> {
            for (Texture texture : sprite.getTextures()) {
                texture.bindTexture();
                if (vertices.isSupportElements()) {
                    glDrawElements(GL_TRIANGLES, ((ElementsBasedVertexArray) vertices).getElementsSize(),
                            GL_UNSIGNED_INT, 0);
                } else {
                    glDrawArrays(GL_TRIANGLES, 0, vertices.getSize());
                }
            }
        });

        ATTR_BOUNDS.disable();
        ATTR_TEXTURE_CORDS.disable();
        vertices.unbind();
    }

    @Override
    public void destroy() {
        super.destroy();
        sprite.destroy();
        vertices.destroy();
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

    @Override
    public int compareTo(Entity o) {
        if (o instanceof RenderableEntity) {
            int depthComp = Float.compare(depth, ((RenderableEntity) o).depth);
            int sprComp = sprite.compareTo(((RenderableEntity) o).sprite);
            return depthComp == 0 ? sprComp : depthComp;
        }

        return super.compareTo(o);
    }
}
