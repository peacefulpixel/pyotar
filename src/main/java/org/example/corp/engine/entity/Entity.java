package org.example.corp.engine.entity;

import org.example.corp.engine.Layer;
import org.example.corp.engine.base.Destroyable;

public abstract class Entity implements Comparable<Entity>, Destroyable {
    public final int id = hashCode();
    protected Layer layer;

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    @Override
    public void destroy() {
        layer.removeEntity(this);
    }

    @Override
    public int compareTo(Entity o) {
        return 0;
    }
}
