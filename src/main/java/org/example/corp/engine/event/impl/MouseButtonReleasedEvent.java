package org.example.corp.engine.event.impl;

import org.example.corp.engine.controls.MouseButton;

public class MouseButtonReleasedEvent extends MouseEvent {
    public final MouseButton button;

    public MouseButtonReleasedEvent(double x, double y, MouseButton button) {
        super(x, y);
        this.button = button;
    }
}
