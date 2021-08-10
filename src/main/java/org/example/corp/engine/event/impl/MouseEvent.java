package org.example.corp.engine.event.impl;

import org.example.corp.engine.event.Event;

public abstract class MouseEvent implements Event {
    public final double x;
    public final double y;

    public MouseEvent(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
