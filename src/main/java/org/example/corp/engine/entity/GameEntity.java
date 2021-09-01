package org.example.corp.engine.entity;

import org.example.corp.engine.base.Logical;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;

public abstract class GameEntity extends RenderableEntity implements Logical {

    public GameEntity(Sprite sprite) throws EngineException {
        super(sprite);
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
