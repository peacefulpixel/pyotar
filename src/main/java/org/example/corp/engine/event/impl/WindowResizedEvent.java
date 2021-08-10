package org.example.corp.engine.event.impl;

import org.example.corp.engine.event.Event;

public class WindowResizedEvent implements Event {
    public final int oldWidth;
    public final int oldHeight;
    public final int newWidth;
    public final int newHeight;

//    WindowResizedEvent() {
//        oldWidth = 0;
//        oldHeight = 0;
//        newWidth = 0;
//        newHeight = 0;
//    }

    public WindowResizedEvent(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }
}
