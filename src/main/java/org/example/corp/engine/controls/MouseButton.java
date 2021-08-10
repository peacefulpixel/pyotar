package org.example.corp.engine.controls;

import org.example.corp.engine.util.LoggerUtils;
import org.lwjgl.glfw.GLFW;

import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseButton {
    LEFT_MB  (GLFW_MOUSE_BUTTON_LEFT),
    RIGHT_MB (GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE_MB(GLFW_MOUSE_BUTTON_MIDDLE);

    private static final Logger logger = LoggerUtils.getLogger(Key.class);

    public final int glfwId;
    MouseButton(int glfwId) {
        this.glfwId = glfwId;
    }

    public static MouseButton getMouseButtonByGlfwId(int glfwId) {
        for (MouseButton button : MouseButton.values()) {
            if (button.glfwId == glfwId) {
                return button;
            }
        }

        logger.warning("Unable to find mouse button by glfwId " + glfwId);
        return null;
    }
}
