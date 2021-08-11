package org.example.corp;

import org.example.corp.engine.Camera;
import org.example.corp.engine.Window;
import org.example.corp.engine.entity.GameEntity;
import org.example.corp.engine.event.EventManager;
import org.example.corp.engine.event.impl.MouseMovedEvent;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.res.ResourceManager;

public class TestGameEntity2 extends GameEntity {

    private final float movSpeed = 240.0f;

    public TestGameEntity2() throws EngineException {
        super(new Sprite(ResourceManager.get(Image.class, "nowhere.png")));
    }

    @Override
    public void init() {
        setDepth(0.499f);
        EventManager.addEventListener(MouseMovedEvent.class, e -> {
//            float xDiff = (float) (e.x - e.prevX);
//            float yDiff = (float) (e.y - e.prevY);
//            setX(getX() + xDiff);
//            setY(getY() - yDiff);
            Camera camera = Window.MAIN_WINDOW.getCamera();
            setX((float) e.x - camera.getWidth() / 2 + camera.getX());
            setY((float) -e.y + camera.getHeight() / 2 + camera.getY());
        });
    }

    @Override
    public void loop() {

    }
}
