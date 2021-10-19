package org.example.corp.engine;

import org.example.corp.engine.base.Renderable;
import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.util.LoggerUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class Stage implements Renderable {
    private static final Logger logger = LoggerUtils.getLogger(Stage.class);

    private LinkedList<Layer> layers = new LinkedList<>();
    private Queue<Runnable> tasks = new LinkedBlockingQueue<>();
    private boolean isFrozen = false;

    private boolean isLayerValid(Layer layer) {
        if (layer.getStage() != null) {
            logger.warning("Layer " + layer + " couldn't be added to stage, since it already connected to other " +
                    "one. Ignoring the action.");

            return false;
        }

        return true;
    }

    public void addLayer(Layer layer) {
        if (isLayerValid(layer)) {
            tasks.add(() -> {
                layers.add(layer);
                layer.init();
            });
        }
    }

    public void addLayer(Layer layer, int index) {
        if (isLayerValid(layer)) {
            tasks.add(() -> {
                layers.add(index, layer);
                layer.init();
            });
        }
    }

    /**
     * Stage will not render anymore until unfreeze was invoked.
     * When stage is in frozen state, layers on it will not be updated, which means that entities on these layers
     * will not be updated too.
     * Also, all the stage tasks (e.g add layer, remove layer) will not be executed until stage is frozen.
     * All of these will be applied next cycle loop, after the stage was frozen.
     */
    public void freeze() {
        isFrozen = true;
    }

    public void unfreeze() {
        isFrozen = false;
    }

    private void update() {
        //TODO: DEPTH ORDER
        for (Layer layer : layers) {
            layer.loop();
            layer.render();
        }
    }

    private void executeTasks() {
        Runnable task;
        while ((task = tasks.poll()) != null) {
            task.run();
        }
    }

    @Override
    public void render() {
        if (!isFrozen) {
            update();
            executeTasks();
        }
    }

    public void free() {
        tasks.add(() -> {
            for (Layer layer : layers) {
                layer.free();
            }
        });
    }
}
