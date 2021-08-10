package org.example.corp.engine;

import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.CameraMovedEvent;
import org.example.corp.engine.event.impl.CameraResizedEvent;
import org.example.corp.engine.exception.ShaderInitializationException;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Camera {
    private final Logger logger = LoggerUtils.getLogger(Camera.class);

    private float x = .0F;
    private float y = .0F;
    private float width;
    private float height;
    private double aspect;

    public Camera(float width, float height) {
        this.width = width;
        this.height = height;
        refreshAspect();
    }

    public Camera() {
        this(Window.MAIN_WINDOW.getWidth(), Window.MAIN_WINDOW.getHeight());
    }

    private void refreshAspect() {
        aspect = width / height;
    }

    private void onResize() {
        refreshAspect();
        Window.MAIN_WINDOW.refreshViewport();
    }

    private void onMove() {
        try {
            ShaderProgramsManager.getDefaultProgram().setUniform("camera_position", x, y);
        } catch (ShaderInitializationException e) {
            logger.log(Level.WARNING, "Unable to refresh camera_position uniform", e);
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        EventManager.provideEvent(new CameraMovedEvent(this.x, y, x, y));
        this.x = x;
        onMove();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        EventManager.provideEvent(new CameraMovedEvent(x, this.y, x, y));
        this.y = y;
        onMove();
    }

    public void setPosition(float x, float y) {
        EventManager.provideEvent(new CameraMovedEvent(this.x, this.y, x, y));
        this.x = x;
        this.y = y;
        onMove();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        EventManager.provideEvent(new CameraResizedEvent(this.width, height, width, height));
        this.width = width;
        onResize();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        EventManager.provideEvent(new CameraResizedEvent(width, this.height, width, height));
        this.height = height;
        onResize();
    }

    public void setSize(float width, float height) {
        EventManager.provideEvent(new CameraResizedEvent(this.width, this.height, width, height));
        this.width = width;
        this.height = height;
        onResize();
    }

    public double getAspect() {
        return aspect;
    }
}
