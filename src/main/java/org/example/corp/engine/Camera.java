package org.example.corp.engine;

import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.CameraMovedEvent;
import org.example.corp.engine.event.impl.CameraResizedEvent;
import org.example.corp.engine.shader.ShaderProgramsManager;
import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Logger;

public class Camera {
    private static final Logger logger = LoggerUtils.getLogger(Camera.class);
    private static Camera currentCamera;

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

    public void activate() {
        if (!isActive()) {
            if (currentCamera != null) currentCamera.deactivate();
            currentCamera = this;
        }

        ShaderProgramsManager.bindAndPerform(program -> program.setCameraPosition(x, y));
    }

    public void deactivate() {
    }

    public boolean isActive() {
        return this == currentCamera;
    }

    private void refreshAspect() {
        aspect = width / height;
    }

    public boolean isPointVisible(float x, float y) {
        float camX = this.x - (width / 2);
        float camY = this.y - (height / 2);

        return !(x > camX + width) && !(x < camX) && !(y > camY + height) && !(y < camY);
    }

    private void onResize() {
        refreshAspect();
        Window.MAIN_WINDOW.refreshViewport();
    }

    private void onMove() {
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
