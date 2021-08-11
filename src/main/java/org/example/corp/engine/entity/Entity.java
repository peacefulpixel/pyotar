package org.example.corp.engine.entity;

import org.example.corp.engine.World;

public abstract class Entity {
    public final int id = hashCode();
    protected World world;

    public void setWorld(World world) {
        this.world = world;
    }

    public void destroy() {
        world.removeEntity(this);
    }
}
