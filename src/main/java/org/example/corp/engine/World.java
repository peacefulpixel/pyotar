package org.example.corp.engine;

import org.example.corp.engine.entity.Entity;
import org.example.corp.engine.entity.Logical;
import org.example.corp.engine.entity.Renderable;
import org.example.corp.engine.entity.RenderableEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class World implements Renderable, Logical {

    protected final List<Entity> entities;
    private boolean isAutoSortingEnabled = true;

    protected boolean doAutoSorting = false;
    private boolean isEntityListFree = true;

    protected final Queue<Entity> entitiesToAdd = new LinkedBlockingQueue<>();
    protected final Queue<Entity> entitiesToRemove = new LinkedBlockingQueue<>();

    public World() {
        entities = new LinkedList<>();
    }

    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
        doAutoSorting = true;
    }

    public void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    public void removeEntities(Class<? extends Entity> clazz) {
        synchronized (entities) {
            for (Entity entity : entities) {
                if (entity.getClass().equals(clazz)) {
                    entitiesToRemove.add(entity);
                }
            }
        }
    }

    public void destroyEntities(Class<? extends Entity> clazz) {
        synchronized (entities) {
            for (Entity entity : entities) {
                if (entity.getClass().equals(clazz)) {
                    entity.destroy();
                    entitiesToRemove.add(entity);
                }
            }
        }
    }

    public void free() {
        synchronized (entities) {
            for (Entity entity : entities) {
                entity.destroy();
            }
        }

        synchronized (entitiesToAdd) {
            for (Entity entity : entitiesToAdd) {
                entity.destroy();
            }
        }

        synchronized (entitiesToRemove) {
            if (!entitiesToRemove.isEmpty()) {
                Entity entity;
                while ((entity = entitiesToRemove.poll()) != null) {
                    entities.remove(entity);
                }
            }
        }
    }

    /**
     * Sorts all renderable entities by depth.
     * Notice that all renderable entities should be always sorted by depth to avoid visual artifacts that caused by
     * default shaders and OpenGL DEPTH_TEST and BLEND functions. GL depth testing is working with model bounds which
     * is always square and not counting fragment alpha channel, so objects behind should be rendered first.
     * You may disable auto-sorting with {@link World#setAutoSortingEnabled(boolean)} method, but make sure you
     * understand the issues it might cause.
     */
    public void sortByDepth() {
        entities.sort((a, b) -> {
            float aDepth = a instanceof RenderableEntity ? ((RenderableEntity) a).getDepth() : .0f;
            float bDepth = b instanceof RenderableEntity ? ((RenderableEntity) b).getDepth() : .0f;
            return Float.compare(aDepth, bDepth);
        });
    }

    @Override
    public void init() {
        synchronized (entities) {
            for (Entity entity : entities) {
                if (entity instanceof Logical)
                    ((Logical) entity).init();
            }
        }
    }

    @Override
    public void loop() {
        synchronized (entities) {
            for (Entity entity : entities) {
                if (entity instanceof Logical)
                    ((Logical) entity).loop();
            }
        }
        synchronized (entitiesToAdd) {
            if (!entitiesToAdd.isEmpty()) {
                Entity entity;
                while ((entity = entitiesToAdd.poll()) != null) {
                    entities.add(entity);
                    entity.setWorld(this);

                    if (entity instanceof Logical)
                        ((Logical) entity).init();
                }
            }
        }
        synchronized (entitiesToRemove) {
            if (!entitiesToRemove.isEmpty()) {
                Entity entity;
                while ((entity = entitiesToRemove.poll()) != null) {
                    entities.remove(entity);
                }
            }
        }
    }

    @Override
    public void render() {
        synchronized (entities) {
            if (isAutoSortingEnabled && doAutoSorting) {
                doAutoSorting = false;
                sortByDepth();
            }

            for (Entity entity : entities) {
                if (entity instanceof Renderable)
                    ((Renderable) entity).render();
            }
        }
    }

    public boolean isAutoSortingEnabled() {
        return isAutoSortingEnabled;
    }

    public void setAutoSortingEnabled(boolean autoSortingEnabled) {
        isAutoSortingEnabled = autoSortingEnabled;
    }
}
