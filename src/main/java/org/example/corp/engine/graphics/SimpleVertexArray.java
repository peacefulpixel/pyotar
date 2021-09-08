package org.example.corp.engine.graphics;

import org.example.corp.engine.shader.Attribute;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class SimpleVertexArray implements VertexArray {

    private int size = 0;
    private Map<Attribute, FloatBuffer> buffers = new HashMap<>();

    public SimpleVertexArray(int size) {
        this.size = size;
    }

    @Override
    public void setBuffer(Attribute attribute, float[] values) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(values.length).put(values);
        buffer.flip();

        buffers.put(attribute, buffer);
    }

    @Override
    public float[] getBuffer(Attribute attribute) {
        return buffers.get(attribute).array();
    }

    @Override
    public void destroy() {
        for (FloatBuffer buffer : buffers.values()) {
            MemoryUtil.memFree(buffer);
        }
    }

    @Override
    public void bind() {
        buffers.forEach((attribute, buffer) -> glVertexAttribPointer(
                attribute.getId(), attribute.getSize(), attribute.getGlType(), false, 0, buffer));
    }

    @Override
    public void unbind() {
        for (Attribute attribute : buffers.keySet()) {
            glDisableVertexAttribArray(attribute.getId());
        }
    }

    @Override
    public boolean isSupportElements() {
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }
}
