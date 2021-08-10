package org.example.corp.engine;


import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.FpsChangedEvent;

public class GameTime {
    public static final long startTime = currentTime();
    public static long previousFrameTime = startTime;
    public static float deltaTime = .0F;
    public static int fps = 0;

    private static int newFps = 0;
    private static long lastSecond = currentTime() / 1000;

    public static synchronized void refreshDelta() {
        // Delta calculation
        long currentTime = currentTime();
        deltaTime = (currentTime - previousFrameTime) / 1000.0F;
        previousFrameTime = currentTime;

        // FPS calculation
        long newSecond = currentTime() / 1000;
        if (lastSecond < newSecond) {
            lastSecond = newSecond;
            EventManager.provideEvent(new FpsChangedEvent(fps, newFps));
            fps = newFps;
            newFps = 0;
        }

        newFps++;
    }

    public static long currentTime() {
        return System.currentTimeMillis();
    }
}
