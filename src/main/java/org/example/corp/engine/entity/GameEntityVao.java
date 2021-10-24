package org.example.corp.engine.entity;

import org.example.corp.engine.base.Logical;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;

public abstract class GameEntityVao extends VaoSquareEntity implements Logical {

    public GameEntityVao(Sprite sprite) throws EngineException {
        super(sprite);
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
