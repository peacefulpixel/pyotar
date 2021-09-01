package org.example.corp.engine.entity;

import org.example.corp.engine.World;
import org.example.corp.engine.base.Destroyable;

public abstract class Entity implements Comparable<Entity>, Destroyable {
    public final int id = hashCode();
    protected World world;

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void destroy() {
        world.removeEntity(this);
    }

    @Override
    public int compareTo(Entity o) {
        return 0;
    }
}
