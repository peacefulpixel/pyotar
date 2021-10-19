package org.example.corp.engine;

import org.example.corp.engine.base.Logical;
import org.example.corp.engine.base.Renderable;
import org.example.corp.engine.entity.Entity;
import org.example.corp.engine.entity.RenderableEntity;
import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.shader.DefaultShaderProgram;
import org.example.corp.engine.shader.ShaderProgram;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.LoggerUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class Layer implements Renderable, Logical {

    private static final Logger logger = LoggerUtils.getLogger(Layer.class);

    private Stage stage;
    private ShaderProgram shaderProgram;
    private Class<? extends ShaderProgram> shaderClass;
    protected final List<Entity> entities;
    private boolean isAutoSortingEnabled = true;

    protected boolean doAutoSorting = false;

    protected final Queue<Entity> entitiesToAdd = new LinkedBlockingQueue<>();
    protected final Queue<Entity> entitiesToRemove = new LinkedBlockingQueue<>();

    public static final float DEPTH_STEP = 1e-8f;

    public Layer(Class<? extends ShaderProgram> shaderClass) {
        entities = new LinkedList<>();
        this.shaderClass = shaderClass;
    }

    public Layer() {
        this(DefaultShaderProgram.class);
    }

    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
        doAutoSorting = true;
    }

    public void addEntities(Entity... entities) {
        entitiesToAdd.addAll(Arrays.asList(entities));
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
     * You may disable auto-sorting with {@link Layer#setAutoSortingEnabled(boolean)} method, but make sure you
     * understand the issues it might cause.
     */
    public void sortEntities() {
        entities.sort(Entity::compareTo);
    }

    public synchronized float nextTopDepth() {
        List<Entity> allEntities =
                Stream.concat(entities.stream(), entitiesToAdd.stream()).filter(e -> e instanceof RenderableEntity)
                .sorted((a, b) -> Float.compare(((RenderableEntity) a).getDepth(), ((RenderableEntity) b).getDepth()))
                .collect(Collectors.toList());

        if (allEntities.isEmpty()) {
            return 0.0f;
        } else {
            Entity entity = allEntities.get(allEntities.size() - 1);
            return ((RenderableEntity) entity).getDepth() + DEPTH_STEP;
        }
    }

    @Override
    public void init() {
        if (shaderClass == null) {
            logger.warning("Shader class is null in layer " + this);
        } else if (shaderProgram == null) {
            try {
                shaderProgram = ShaderProgramsManager.getShaderProgram(shaderClass);
            } catch (ShaderInitializationException e) {
                logger.log(SEVERE, "Unable to retrieve shader program of class " + shaderClass.toString(), e);
            }
        }

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
                    entity.setLayer(this);

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
                sortEntities();
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }
}
