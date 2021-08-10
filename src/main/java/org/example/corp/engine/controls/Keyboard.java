package org.example.corp.engine.controls;

import java.util.HashMap;
import java.util.Map;

import static org.example.corp.engine.controls.KeyAction.*;

public class Keyboard {
    private static final Map<Key, KeyAction> keys = new HashMap<>();

    public static void setKeyAction(Key key, KeyAction action) {
        keys.put(key, action);
    }

    public static boolean isKeyPressed(Key key) {
        return keys.get(key) == PRESSED || isKeyRepeat(key);
    }

    public static boolean isKeyRepeat(Key key) {
        return keys.get(key) == REPEAT;
    }
}
