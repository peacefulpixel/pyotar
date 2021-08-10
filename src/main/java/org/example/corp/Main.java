package org.example.corp;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.World;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.WindowResizedEvent;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.Game;

public class Main {

    public static void main(String[] args) {
        World world = new World();
        Game game = new Game(world);
        try {
            game.init();
        } catch (EngineException e) {
            e.printStackTrace();
        }

        Camera camera = new Camera();
        EventManager.addEventListener(WindowResizedEvent.class, e -> {
            camera.setSize(e.newWidth, e.newHeight);
        });

        Window.MAIN_WINDOW.setCamera(camera);
        world.addEntity(new InitialLogicalEntity());

        try {
            game.start();
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
