package org.example.corp.engine.graphics;

public abstract class ElementsBasedVertexArray implements VertexArray {
    public abstract int getElementsSize();

    @Override
    public boolean isSupportElements() {
        return true;
    }
}
