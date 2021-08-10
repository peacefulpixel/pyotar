package org.example.corp.engine.event.impl;

import org.example.corp.engine.event.Event;

public class CameraResizedEvent implements Event {
    public final float prevWidth;
    public final float prevHeight;
    public final float width;
    public final float height;

    public CameraResizedEvent(float prevWidth, float prevHeight, float width, float height) {
        this.prevWidth = prevWidth;
        this.prevHeight = prevHeight;
        this.width = width;
        this.height = height;
    }
}
