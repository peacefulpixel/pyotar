package org.example.corp.engine.event.impl;

import org.example.corp.engine.event.Event;

public class CameraMovedEvent implements Event {
    public final float prevX;
    public final float prevY;
    public final float x;
    public final float y;

    public CameraMovedEvent(float prevX, float prevY, float x, float y) {
        this.prevX = prevX;
        this.prevY = prevY;
        this.x = x;
        this.y = y;
    }
}
