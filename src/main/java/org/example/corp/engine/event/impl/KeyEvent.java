package org.example.corp.engine.event.impl;

import org.example.corp.engine.controls.Key;
import org.example.corp.engine.event.Event;

import static org.lwjgl.glfw.GLFW.*;

public abstract class KeyEvent implements Event {
    public final Key key;
    public final boolean isShiftPressed;
    public final boolean isControlPressed;
    public final boolean isAltPressed;
    public final boolean isSuperPressed;
    public final boolean isCapsLockToggled;
    public final boolean isNumLockToggled;

    public KeyEvent(Key key, boolean isShiftPressed, boolean isControlPressed,
                    boolean isAltPressed, boolean isSuperPressed, boolean isCapsLockToggled, boolean isNumLockToggled) {
        this.key = key;
        this.isShiftPressed = isShiftPressed;
        this.isControlPressed = isControlPressed;
        this.isAltPressed = isAltPressed;
        this.isSuperPressed = isSuperPressed;
        this.isCapsLockToggled = isCapsLockToggled;
        this.isNumLockToggled = isNumLockToggled;
    }

    public KeyEvent(Key key, int glfwMods) {
        this.key = key;

        isShiftPressed =    (glfwMods & GLFW_MOD_SHIFT)     != 0;
        isControlPressed =  (glfwMods & GLFW_MOD_CONTROL)   != 0;
        isAltPressed =      (glfwMods & GLFW_MOD_ALT)       != 0;
        isSuperPressed =    (glfwMods & GLFW_MOD_CONTROL)   != 0;
        isCapsLockToggled = (glfwMods & GLFW_MOD_CAPS_LOCK) != 0;
        isNumLockToggled =  (glfwMods & GLFW_MOD_NUM_LOCK)  != 0;
    }

    public KeyEvent(Key key) {
        this.key = key;

        isShiftPressed = isControlPressed = isAltPressed = isSuperPressed = isCapsLockToggled =
                isNumLockToggled = false;
    }
}
