package org.example.corp;

import org.example.corp.engine.World;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.Game;

public class Main {

    public static void main(String[] args) {
        World world = new World();
        Game game = new Game(world);
        world.addEntity(new InitialLogicalEntity());
        try {
            game.start();
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }
}
