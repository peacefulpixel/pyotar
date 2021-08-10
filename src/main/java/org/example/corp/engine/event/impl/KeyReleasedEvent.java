package org.example.corp.engine.event.impl;

import org.example.corp.engine.controls.Key;

public class KeyReleasedEvent extends KeyEvent {

    public KeyReleasedEvent(Key key, boolean isShiftPressed, boolean isControlPressed, boolean isAltPressed, boolean isSuperPressed, boolean isCapsLockToggled, boolean isNumLockToggled) {
        super(key, isShiftPressed, isControlPressed, isAltPressed, isSuperPressed, isCapsLockToggled, isNumLockToggled);
    }

    public KeyReleasedEvent(Key key, int glfwMods) {
        super(key, glfwMods);
    }

    public KeyReleasedEvent(Key key) {
        super(key);
    }
}
