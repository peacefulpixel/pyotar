package org.example.corp.engine.graphics;

import org.example.corp.engine.shader.Attribute;
import org.example.corp.engine.util.GLUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * VAO based vertex array that could have multiple buffers.
 * IMPORTANT: This array only supports fixed vertices array size by default, since attributes are not being refreshed
 *            during vertices array or any buffer replacement. glVertexAttribPointer defines vertices array size which
 *            should be read by video adapter, so it will always read the same size unless you re-create VAO based
 *            vertex array.
 */
public class VaoVertexArray extends ElementsBasedVertexArray {

    private final int vaoId;
    private final int eboId;

    private int[] elements;
    private final int size;

    private Map<Attribute, Integer> vboIds = new HashMap<>(2);
    private Map<Attribute, float[]> vertexArrays = new HashMap<>(2);

    public VaoVertexArray(Attribute verticesAttribute, float[] vertices, int[] elements) {
        this.elements = elements;
        size = vertices.length;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        addBufferWithoutBindings(verticesAttribute, vertices);

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        GLUtils.glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        glBindVertexArray(vaoId);
        for (int vboId : vboIds.values()) {
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
        for (int vboId : vboIds.values()) {
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public int[] getElements() {
        return elements;
    }

    public void setElements(int[] elements) {
        this.elements = elements;

        glBindVertexArray(vaoId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        GLUtils.glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);
        glBindVertexArray(0);
    }

    protected void addBufferWithoutBindings(Attribute attribute, float[] values) {
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        GLUtils.glBufferData(GL_ARRAY_BUFFER, values, GL_STATIC_DRAW);
        glVertexAttribPointer(attribute.getId(), attribute.getSize(), attribute.getGlType(), false, 0, 0);

        vboIds.put(attribute, vboId);
        vertexArrays.put(attribute, values);
    }

    @Override
    public float[] getBuffer(Attribute attribute) {
        return vertexArrays.get(attribute);
    }

    @Override
    public void setBuffer(Attribute attribute, float[] values) {
        glBindVertexArray(vaoId);

        Integer oldVbo;
        if ((oldVbo = vboIds.get(attribute)) != null) {
            glDeleteBuffers(oldVbo);
        }

        addBufferWithoutBindings(attribute, values);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getElementsSize() {
        return elements.length;
    }
}
