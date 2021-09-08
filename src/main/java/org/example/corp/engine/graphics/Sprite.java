package org.example.corp.engine.graphics;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.base.Destroyable;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.shader.DefaultShaderProgram;
import org.example.corp.engine.shader.ShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.BufferUtils;
import org.example.corp.engine.util.LoggerUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.toRadians;
import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_BOUNDS;
import static org.example.corp.engine.shader.DefaultShaderProgram.ATTR_TEXTURE_CORDS;
import static org.example.corp.engine.shader.ShaderProgramsManager.DEFAULT_PROGRAM;
import static org.lwjgl.opengl.GL30.*;

public class Sprite implements Comparable<Sprite>, Destroyable {

    private final Logger logger = LoggerUtils.getLogger(Sprite.class);

    private static final float[] textureCordsWithElements = new float[] {
            0.0f, 0.0f, // Top-left
            1.0f, 0.0f, // Top-right
            1.0f, 1.0f, // Bottom-right
            0.0f, 1.0f,  // Bottom-left
    };

    private static final float[] textureCordsNoElements = new float[] {
            0.0f, 0.0f, // Top-left
            1.0f, 0.0f, // Top-right
            1.0f, 1.0f, // Bottom-right
            1.0f, 1.0f, // Bottom-right
            0.0f, 1.0f,  // Bottom-left
            0.0f, 0.0f, // Top-left
    };

    private float[] textureCords = textureCordsWithElements;

    private Texture[] textures;
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

    private DefaultShaderProgram shaderProgram = ShaderProgramsManager.getShaderProgram(DefaultShaderProgram.class);

    /**
     * @see Sprite#Sprite(float, Texture...)
     */
    public Sprite(Texture ...textures) throws EngineException {
        this(4.0f, textures);
    }

    /**
     * Constructor
     * @param textures Textures?
     * @param scaling Texture scaling (4x by default)
     */
    public Sprite(float scaling, Texture ...textures) throws EngineException {
        if (textures.length < 1) {
            throw new EngineException("Unable to initialize sprite without any texture");
        }

        this.textures = textures;
        color = new Vector3f(1.0f, 1.0f, 1.0f);

        spriteOriginalWidth  = textures[0].width;
        spriteOriginalHeight = textures[0].height;

        width  = spriteOriginalWidth  * scaling;
        height = spriteOriginalHeight * scaling;

        setAxisToMiddle();

        rotation = 180.0d;

        rebuildTransformationMatrix();
    }

    public void bind(VertexArray vertexArray) throws EngineException {
        if (vaoId != 0) {
            throw new EngineException("Unable to bind texture, since it's already bound. Old VAO ID: " + vaoId);
        }

        if (!vertexArray.isSupportElements()) {
            textureCords = textureCordsNoElements;
        }

        vertexArray.setBuffer(ATTR_TEXTURE_CORDS, textureCords);
    }

    @Override
    public void destroy() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(textureCordsVBOId);
        for (Texture texture : textures) {
            texture.destroy();
        }

//        vaoId = 0;
    }

    public void setUniforms() {
        shaderProgram.setTexture2d(0);
        shaderProgram.setTextureColor(color.x, color.y, color.z); // TODO: ?
        shaderProgram.setTransformation(transformation);
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

    public Texture[] getTextures() {
        return textures;
    }

    @Override
    public int compareTo(Sprite o) {
        // TODO: It's not work when sprite have multiple textures
//        return texture.compareTo(o.texture);
        return 0;
    }
}
