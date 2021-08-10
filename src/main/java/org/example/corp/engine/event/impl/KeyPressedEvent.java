package org.example.corp.engine.event.impl;

import org.example.corp.engine.controls.Key;

public class KeyPressedEvent extends KeyEvent {

    public KeyPressedEvent(Key key, boolean isShiftPressed, boolean isControlPressed, boolean isAltPressed,
                           boolean isSuperPressed, boolean isCapsLockToggled, boolean isNumLockToggled) {
        super(key, isShiftPressed, isControlPressed, isAltPressed, isSuperPressed, isCapsLockToggled, isNumLockToggled);
    }

    public KeyPressedEvent(Key key, int glfwMods) {
        super(key, glfwMods);
    }

    public KeyPressedEvent(Key key) {
        super(key);
    }
}
