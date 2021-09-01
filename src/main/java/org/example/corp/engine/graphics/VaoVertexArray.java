package org.example.corp.engine.graphics;

import org.example.corp.engine.shader.Attribute;
import org.example.corp.engine.util.BufferUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * VAO based vertex array that could have multiple buffers.
 * IMPORTANT: This array only supports fixed vertices array size by default, since attributes are not being refreshed
 *            during vertices array or any buffer replacement. glVertexAttribPointer defines vertices array size which
 *            should be read by video adapter, so it will always read the same size unless you re-create VAO based
 *            vertex array.
 */
public class VaoVertexArray implements VertexArray {

    private final int vaoId;
    private final int eboId;

    private float[] vertices;
    private int[] elements;

    private List<Integer> vboIds = new ArrayList<>(2);

    public VaoVertexArray(Attribute verticesAttribute, float[] vertices, int[] elements) {
        this.vertices = vertices;
        this.elements = elements;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        addBufferWithoutBindings(verticesAttribute, vertices);

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        BufferUtils.glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        glBindVertexArray(vaoId);
        for (int vboId : vboIds) {
            glDeleteBuffers(vboId);
        }
        glDeleteBuffers(eboId);
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    @Override
    public void bind() {
        glBindVertexArray(vaoId);
    }

    public void bindBuffers() {
        for (int vboId : vboIds) {
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void setVertices(float[] vertices) {
        this.vertices = vertices;

        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vboIds.get(0));
        BufferUtils.glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public float[] getVertices() {
        return vertices;
    }

    public int[] getElements() {
        return elements;
    }

    public void setElements(int[] elements) {
        this.elements = elements;

        glBindVertexArray(vaoId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        BufferUtils.glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);
        glBindVertexArray(0);
    }

    protected void addBufferWithoutBindings(Attribute attribute, float[] values) {
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        BufferUtils.glBufferData(GL_ARRAY_BUFFER, values, GL_STATIC_DRAW);
        glVertexAttribPointer(attribute.getId(), attribute.getSize(), attribute.getGlType(), false, 0, 0);

        vboIds.add(vboId);
    }

    public void addBuffer(Attribute attribute, float[] values) {
        glBindVertexArray(vaoId);

        addBufferWithoutBindings(attribute, values);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
