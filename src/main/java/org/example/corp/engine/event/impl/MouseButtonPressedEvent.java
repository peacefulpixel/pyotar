package org.example.corp.engine.event.impl;

import org.example.corp.engine.controls.MouseButton;

public class MouseButtonPressedEvent extends MouseEvent {
    public final MouseButton button;

    public MouseButtonPressedEvent(double x, double y, MouseButton button) {
        super(x, y);
        this.button = button;
    }
}
