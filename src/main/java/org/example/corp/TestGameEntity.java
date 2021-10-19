package org.example.corp;

import org.example.corp.engine.Random;
import org.example.corp.engine.entity.GameEntity;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;
import org.example.corp.engine.graphics.Texture;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.res.ResourceManager;

import static org.example.corp.engine.GameTime.deltaTime;

public class TestGameEntity extends GameEntity {

    private float movSpeed, rotationSpeed;
    private boolean moveX;

    static Texture t1;
    static Texture t2;
    static Texture t3;

    private static Texture getRandomTexture() {
        if (t1 == null) {
            t1 = new Texture(ResourceManager.get(Image.class, "res/img/test_building1.png"));
            t2 = new Texture(ResourceManager.get(Image.class, "res/img/test_building2.png"));
            t3 = new Texture(ResourceManager.get(Image.class, "res/img/test_building3.png"));
        }
        long rn = Random.genUnsigned() % 3 + 1;
        if (rn == 1) return t1;
        if (rn == 2) return t2;
        return t3;
    }

    public TestGameEntity() throws EngineException {
        super(new Sprite(0.1f * (Random.gen() % 40), getRandomTexture()));
        moveX = Random.genUnsigned() % 2 == 1;
        movSpeed = Random.gen() % 120;
        rotationSpeed = Random.gen() % 90;
    }

    @Override
    public void init() {
        setDepth(layer.nextTopDepth());
    }

    @Override
    public void loop() {
        Sprite sprite = getSprite();
        if (moveX) setX(getX() + movSpeed * deltaTime);
        else setY(getY() + movSpeed * deltaTime);

        sprite.setRotation(sprite.getRotation() + rotationSpeed * deltaTime);
    }
}
