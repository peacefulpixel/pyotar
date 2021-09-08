package org.example.corp.engine.graphics;

import org.example.corp.engine.base.Destroyable;
import org.example.corp.engine.shader.Attribute;

public interface VertexArray extends Destroyable {
    void bind();
    void unbind();
    void setBuffer(Attribute attribute, float[] values);
    float[] getBuffer(Attribute attribute);
    boolean isSupportElements();
    int getSize();
}
