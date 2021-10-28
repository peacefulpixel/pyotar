package org.example.corp;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.controls.Key;
import org.example.corp.engine.controls.Keyboard;
import org.example.corp.engine.entity.LogicalEntity;
import org.example.corp.engine.exception.EngineException;

import static org.example.corp.engine.GameTime.deltaTime;

public class InitialLogicalEntity extends LogicalEntity {

    private final float movSpeed = 240.0F;

    @Override
    public void init() {
        try {
            layer.addEntity(new TestGameEntity());
            layer.addEntity(new TestGameEntity());
            layer.addEntity(new TestGameEntity2());
        } catch (EngineException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loop() {
        Camera camera = Window.MAIN_WINDOW.getCamera();

        float movSpeed = this.movSpeed;
        if (Keyboard.isKeyPressed(Key.KEY_LEFT_SHIFT)) {
            movSpeed *= 2;
        }

        if (Keyboard.isKeyPressed(Key.KEY_W)) {
            camera.setY(camera.getY() + movSpeed * deltaTime);
        }

        if (Keyboard.isKeyPressed(Key.KEY_S)) {
            camera.setY(camera.getY() - movSpeed * deltaTime);
        }

        if (Keyboard.isKeyPressed(Key.KEY_A)) {
            camera.setX(camera.getX() - movSpeed * deltaTime);
        }

        if (Keyboard.isKeyPressed(Key.KEY_D)) {
            camera.setX(camera.getX() + movSpeed * deltaTime);
        }
    }
}
