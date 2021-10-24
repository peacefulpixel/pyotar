package org.example.corp.engine.entity;

import org.example.corp.engine.base.Logical;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;

public abstract class GameEntityVao extends VaoSquareEntity implements Logical {

    public GameEntityVao(Texture ...textures) throws EngineException {
        super(textures);
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
