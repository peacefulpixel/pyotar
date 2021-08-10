package org.example.corp.engine.event.impl;

public class MouseScrollEvent extends MouseEvent {
    public final boolean isUp;

    public MouseScrollEvent(double x, double y, boolean isUp) {
        super(x, y);
        this.isUp = isUp;
    }
}
