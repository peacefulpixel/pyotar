package org.example.corp.engine.event.impl;

public class MouseMovedEvent extends MouseEvent {
    public final double prevX;
    public final double prevY;

    public MouseMovedEvent(double x, double y, double prevX, double prevY) {
        super(x, y);
        this.prevX = prevX;
        this.prevY = prevY;
    }
}
