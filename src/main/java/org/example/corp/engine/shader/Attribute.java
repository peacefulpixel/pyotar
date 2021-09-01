package org.example.corp.engine.shader;

import org.example.corp.engine.FinalObject;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class Attribute {
    private final FinalObject<Integer> id = new FinalObject<>();
    private final String name;
    private final int size;
    private final int glType;

    public Attribute(String name, int size, int glType) {
        this.name = name;
        this.size = size;
        this.glType = glType;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getId() {
        return id.isInitialized() ? id.get(): 0;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getGlType() {
        return glType;
    }

    public void enable() {
        glEnableVertexAttribArray(id.get());
    }

    public void disable() {
        glDisableVertexAttribArray(id.get());
    }
}
