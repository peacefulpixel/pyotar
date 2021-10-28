package org.example.corp.engine.entity;

import org.example.corp.engine.Layer;
import org.example.corp.engine.base.Destroyable;

public abstract class Entity implements Comparable<Entity>, Destroyable {
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

    protected <T extends Entity> void cloneTo(T dest) {
        dest.setLayer(layer);
    }
}
