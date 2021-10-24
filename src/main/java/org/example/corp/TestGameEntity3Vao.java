package org.example.corp;

import org.example.corp.engine.entity.GameEntityVao;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;

public class TestGameEntity3Vao extends GameEntityVao {

    public TestGameEntity3Vao(Texture ...textures) throws EngineException {
        super(textures);
    }
}
