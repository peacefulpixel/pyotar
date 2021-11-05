package org.example.corp.engine.entity;

import org.example.corp.engine.Camera;
import org.example.corp.engine.base.Renderable;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.ElementsBasedVertexArray;
import org.example.corp.engine.graphics.Texture;
import org.example.corp.engine.graphics.VertexArray;
import org.example.corp.engine.shader.ShaderProgram;
import org.example.corp.engine.util.GLUtils;
import org.example.corp.engine.util.LoggerUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.toRadians;
import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_BOUNDS;
import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_TEXTURE_CORDS;
import static org.lwjgl.opengl.GL30.*;

public abstract class RenderableEntity extends Entity implements Renderable {

    private static final Logger logger = LoggerUtils.getLogger(RenderableEntity.class);
    private static final float[] textureCordsWithElements = new float[] {
            0.0f, 0.0f, // Top-left
            1.0f, 0.0f, // Top-right
            1.0f, 1.0f, // Bottom-right
            0.0f, 1.0f, // Bottom-left
    };
    private static final float[] textureCordsNoElements = new float[] {
            0.0f, 0.0f, // Top-left
            1.0f, 0.0f, // Top-right
            1.0f, 1.0f, // Bottom-right
            1.0f, 1.0f, // Bottom-right
            0.0f, 1.0f, // Bottom-left
            0.0f, 0.0f, // Top-left
    };


    public static float DEFAULT_SCALING = 4.0f;

    private float x = 0.0f;
    private float y = 0.0f;
    private float depth = 0.0f;
    private Texture[] textures;
    private float width;
    private float height;
    private float axisX;
    private float axisY;
    private Vector3f color;
    private Matrix4f transformation;
    private double rotation;
    private boolean transformationRebuildRequiredFlag = false;

    protected float[] vertexArray;
    protected VertexArray vertices;

    private float[] textureCords = textureCordsWithElements;

    public RenderableEntity(Texture ...textures) throws EngineException {

        if (textures.length < 1) {
            throw new EngineException("Unable to initialize renderable entity without any texture");
        }

        width  = textures[0].width * DEFAULT_SCALING;
        height = textures[0].height * DEFAULT_SCALING;

        setAxisToMiddle();
        rotation = 180.0d;
        rebuildTransformationMatrix();

        refreshVertexArray();
        this.vertices = createVertexArray();

        this.textures = textures;
        color = new Vector3f(1.0f, 1.0f, 1.0f);

        if (!vertices.isSupportElements()) {
            textureCords = textureCordsNoElements;
        }

        vertices.setBuffer(ATTR_TEXTURE_CORDS, textureCords);
    }

    protected abstract void refreshVertexArray();
    protected abstract boolean shouldBeRendered();
    protected abstract VertexArray createVertexArray();

    @Override
    public synchronized void render() {
        if (!shouldBeRendered()) return;

        ShaderProgram shaderProgram = layer.getShaderProgram();
        if (shaderProgram == null) {
            logger.warning("Entity " + this + " is not ready for rendering since the shader program is not set");
            return;
        }

        vertices.bind();
        ATTR_BOUNDS.enable();
        ATTR_TEXTURE_CORDS.enable();

        shaderProgram.setPosition(x, y);
        shaderProgram.setDepth(-depth);
        shaderProgram.setTexture2d(0);
        shaderProgram.setTextureColor(color.x, color.y, color.z); // TODO: ?
        shaderProgram.setTransformation(getTransformation());
        shaderProgram.bindAndPerform(p -> {
            for (Texture texture : textures) {
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

    private void rebuildTransformationMatrix() {
        if (layer == null) {
            transformationRebuildRequiredFlag = true;
            return;
        }

        Camera camera = layer.getCamera();
        transformation = new Matrix4f().ortho2D(-camera.getWidth()/2, camera.getWidth()/2, camera.getHeight()/2,
                -camera.getHeight()/2).rotateZ((float) toRadians(rotation)).scaleXY(-1, 1);
        //TODO: Validate is scaling is right decision to do here
    }

    public void setAxis(float x, float y) {
        if (x < 0.0f || y < 0.0f) {
            logger.log(Level.WARNING, "Attempt to set sprite axis to negative value: x=" + x + ", y=" + y);
            return;
        }
        axisX = x;
        axisY = y;
        refreshVertexArray();
    }

    public void setAxisToMiddle() {
        setAxis(width  / 2.0f, height / 2.0f);
    }

    /**
     * Changes frame coordinates for sprite (0,0,sprite.getWidth(),sprite.getHeight() by default).
     * Technically, changes the texture cords, so all sprites textures will take effect. Not affects a texture itself.
     * @param x Frame X cord
     * @param y Frame Y cord
     * @param w Frame width
     * @param h Frame height
     */
    public void frame(float x, float y, float w, float h) {
        float horPx = 1.0f / width;
        float verPx = 1.0f / height;
        float[] texCords = new float[] {
                x * horPx, y * verPx,                           // Top-left
                x * horPx + w * horPx, y * verPx,              // Top-right
                x * horPx + w * horPx, y * verPx + h * verPx, // Bottom-right
                x * horPx, y * verPx + h * verPx,            // Bottom-left
        };

        if (!vertices.isSupportElements()) {
            texCords = GLUtils.mapVertexArrayOnElements(texCords, GLUtils.SQUARE_ELEMENTS_ARRAY,
                    GLUtils.VECTOR_SIZE_2D);
        }

        textureCords = texCords;
        vertices.setBuffer(ATTR_TEXTURE_CORDS, textureCords);
    }

    private Matrix4f getTransformation() {
        if (transformationRebuildRequiredFlag) {
            rebuildTransformationMatrix();
        }

        return transformation;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        rebuildTransformationMatrix();
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Texture texture : textures) {
            texture.destroy();
        }
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

    public float getAxisX() {
        return axisX;
    }

    public float getAxisY() {
        return axisY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        refreshVertexArray();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        refreshVertexArray();
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setColor(float r, float g, float b) {
        setColor(new Vector3f(r, g, b));
    }

    /**
     * Changes width and height for the entity.
     * Technically faster then followed calls of {@link #setWidth(float)} and {@link #setHeight(float)}, since this
     * method refreshes internal vertex array only once, so use that if you need to change width and height in one time
     * @param width width in pixels
     * @param height height in pixels
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        refreshVertexArray();
    }

    @Override
    public int compareTo(Entity o) {
        if (o instanceof RenderableEntity) {
            return Float.compare(depth, ((RenderableEntity) o).depth);
        }

        return super.compareTo(o);
    }

    protected <T extends RenderableEntity> void cloneTo(T dest) {
        super.cloneTo(dest);
        dest.setX(x);
        dest.setY(y);
        dest.setDepth(0.0f);
        dest.setSize(width, height);
        dest.setAxis(axisX, axisY);
        dest.setColor(color.x, color.y, color.z);
        dest.setRotation(rotation);
    }
}
