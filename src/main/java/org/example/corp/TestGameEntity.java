package org.example.corp;

import org.example.corp.engine.Random;
import org.example.corp.engine.entity.GameEntity;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.res.ResourceManager;

import static org.example.corp.engine.GameTime.deltaTime;

public class TestGameEntity extends GameEntity {

    private float movSpeed, rotationSpeed;
    private boolean moveX;

    public TestGameEntity() throws EngineException {
        super(new Sprite(ResourceManager.get(Image.class, "res/img/test_building.png")));
        moveX = Random.gen() % 2 == 1;
        movSpeed = Random.gen() % 120;
        rotationSpeed = Random.gen() % 90;
        setDepth(((float) Random.gen() % 10) / 10);
    }

    @Override
    public void loop() {
        Sprite sprite = getSprite();
        if (moveX) setX(getX() + movSpeed * deltaTime);
        else setY(getY() + movSpeed * deltaTime);

        sprite.setRotation(sprite.getRotation() + rotationSpeed * deltaTime);
    }
}
