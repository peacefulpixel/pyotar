package org.example.corp.engine.graphics;

import org.example.corp.engine.base.Destroyable;

public interface VertexArray extends Destroyable {
    void bind();
    void unbind();
    void setVertices(float[] vertices);
    float[] getVertices();
}
