package org.example.corp.engine.controls;

import java.util.HashMap;
import java.util.Map;

public class Mouse {
    public static double mouseX = 0; //TODO: Make engine user not to set these values
    public static double mouseY = 0; //TODO: Make engine user not to set these values
    private static final Map<MouseButton, Boolean> buttons = new HashMap<>();

    public static void setButtonPressed(MouseButton button, boolean isPressed) {
        buttons.put(button, isPressed);
    }

    public static boolean isButtonPressed(MouseButton button) {
        Boolean isPressed = buttons.get(button);
        return isPressed != null && isPressed;
    }
}
