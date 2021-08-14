package org.example.corp.engine.graphics;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.shader.ShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.BufferUtils;
import org.example.corp.engine.util.LoggerUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.toRadians;
import static org.example.corp.engine.shader.ShaderProgramsManager.DEFAULT_PROGRAM;
import static org.lwjgl.opengl.GL30.*;

public class Sprite implements Comparable<Sprite> {

    private final Logger logger = LoggerUtils.getLogger(Sprite.class);

    private static final float[] textureCords = new float[] {
            0.0f, 0.0f, // Top-left
            1.0f, 0.0f, // Top-right
            1.0f, 1.0f, // Bottom-right
            0.0f, 1.0f,  // Bottom-left
    };

    private Texture texture;
    private int textureCordsVBOId = 0;
    private int vaoId = 0;

    public final int spriteOriginalWidth;
    public final int spriteOriginalHeight;

    private float width;
    private float height;
    private float axisX;
    private float axisY;
    private Vector3f color;
    private Matrix4f transformation;
    private double rotation;

    private final int attrTextureCords;

    private ShaderProgram shaderProgram = ShaderProgramsManager.getShaderProgram(DEFAULT_PROGRAM);

    /**
     * @see Sprite#Sprite(Texture, float)
     */
    public Sprite(Texture texture) throws EngineException {
        this(texture, 4.0f);
    }

    /**
     * Constructor
     * @param texture PNG image resource file (RGBA, 8bit)
     * @param scaling Texture scaling (4x by default)
     */
    public Sprite(Texture texture, float scaling) throws EngineException {
        this.texture = texture;
        color = new Vector3f(1.0f, 1.0f, 1.0f);

        spriteOriginalWidth  = texture.width;
        spriteOriginalHeight = texture.height;

        width  = spriteOriginalWidth  * scaling;
        height = spriteOriginalHeight * scaling;

        setAxisToMiddle();

        attrTextureCords = shaderProgram.getAttribute("texture_cords");

        rotation = 180.0d;

        rebuildTransformationMatrix();
    }

    public void bind(int newVaoId) throws EngineException {
        if (vaoId != 0) {
            throw new EngineException("Unable to bind texture, since it's already bound. Old VAO ID: " + vaoId);
        }

        vaoId = newVaoId;
        glBindVertexArray(vaoId);

        textureCordsVBOId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureCordsVBOId);
        BufferUtils.glBufferData(GL_ARRAY_BUFFER, textureCords, GL_STATIC_DRAW);
        glVertexAttribPointer(attrTextureCords, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(textureCordsVBOId);
        texture.destroy();

//        vaoId = 0;
    }

    public void bindTexture() {
        texture.bindTexture();
        shaderProgram.setUniform("texture_2d", 0);
        shaderProgram.setUniform("texture_color", color);
        shaderProgram.setUniform("transformation", transformation);
    }

    public void rebuildTransformationMatrix() {
        Camera camera = Window.MAIN_WINDOW.getCamera();
        transformation = new Matrix4f().ortho2D(-camera.getWidth(), camera.getWidth(), camera.getHeight(),
                -camera.getHeight()).rotateZ((float) toRadians(rotation));
    }

    public void setAxis(float x, float y) {
        if (x < 0.0f || y < 0.0f) {
            logger.log(Level.WARNING, "Attempt to set sprite axis to negative value: x=" + x + ", y=" + y);
            return;
        }
        axisX = x;
        axisY = y;
    }

    public void setAxisToMiddle() {
        axisX = width  / 2.0f;
        axisY = height / 2.0f;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        rebuildTransformationMatrix();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAxisX() {
        return axisX;
    }

    public float getAxisY() {
        return axisY;
    }

    @Override
    public int compareTo(Sprite o) {
        return texture.compareTo(o.texture);
    }
}
