package org.example.corp.engine.event.impl;

import org.example.corp.engine.controls.Key;

public class KeyRepeatEvent extends KeyEvent {

    public KeyRepeatEvent(Key key, boolean isShiftPressed, boolean isControlPressed, boolean isAltPressed, boolean isSuperPressed, boolean isCapsLockToggled, boolean isNumLockToggled) {
        super(key, isShiftPressed, isControlPressed, isAltPressed, isSuperPressed, isCapsLockToggled, isNumLockToggled);
    }

    public KeyRepeatEvent(Key key, int glfwMods) {
        super(key, glfwMods);
    }

    public KeyRepeatEvent(Key key) {
        super(key);
    }
}
