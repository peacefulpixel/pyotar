package org.example.corp;

import org.example.corp.engine.controls.Key;
import org.example.corp.engine.controls.Keyboard;
import org.example.corp.engine.entity.GameEntity;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.MouseMovedEvent;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.res.ResourceManager;

import static org.example.corp.engine.GameTime.deltaTime;

public class TestGameEntity2 extends GameEntity {

    private final float movSpeed = 240.0f;

    public TestGameEntity2() throws EngineException {
        super(new Sprite(ResourceManager.get(Image.class, "nowhere.png")));
    }

    @Override
    public void init() {
        EventManager.addEventListener(MouseMovedEvent.class, e -> {
            float xDiff = (float) (e.x - e.prevX);
            float yDiff = (float) (e.y - e.prevY);
            setX(getX() + xDiff);
            setY(getY() - yDiff);
        });
    }

    @Override
    public void loop() {
        if (Keyboard.isKeyPressed(Key.KEY_W)) {
            setY(getY() + movSpeed * deltaTime);
        }

        if (Keyboard.isKeyPressed(Key.KEY_S)) {
            setY(getY() - movSpeed * deltaTime);
        }

        if (Keyboard.isKeyPressed(Key.KEY_A)) {
            setX(getX() - movSpeed * deltaTime);
        }

        if (Keyboard.isKeyPressed(Key.KEY_D)) {
            setX(getX() + movSpeed * deltaTime);
        }
    }
}
