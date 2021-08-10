package org.example.corp.engine.event.impl;

import org.example.corp.engine.event.Event;

public class FpsChangedEvent implements Event {
    public final int oldFps;
    public final int newFps;

    public FpsChangedEvent(int oldFps, int newFps) {
        this.oldFps = oldFps;
        this.newFps = newFps;
    }
}
