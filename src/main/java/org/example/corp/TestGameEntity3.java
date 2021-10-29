package org.example.corp;

import org.example.corp.engine.entity.GameEntity;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;

public class TestGameEntity3 extends GameEntity {

    public TestGameEntity3(Texture ...textures) throws EngineException {
        super(textures);
    }
}
