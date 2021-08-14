package org.example.corp.engine.graphics;

import org.example.corp.engine.res.Image;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture implements Comparable<Texture> {

    private final int textureId;
    public final int width;
    public final int height;

    private static int boundTextureId = 0;

    public Texture(Image image) {
        textureId = glGenTextures();
        width = image.getWidth();
        height = image.getHeight();

        bindTexture();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
//        float[] color = { 1.0f, 1.0f, 1.0f, 0.0f };
//        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, color);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glGenerateMipmap(GL_TEXTURE_2D);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
                GL_UNSIGNED_BYTE, image.getDecodedImage());
    }

    public void bindTexture() {
        if (boundTextureId != textureId) {
            glBindTexture(GL_TEXTURE_2D, textureId);
            boundTextureId = textureId;
        }
    }

    public void destroy() {
        glDeleteTextures(textureId);
    }

    @Override
    public int compareTo(Texture o) {
        return Integer.compare(textureId, o.textureId);
    }
}
