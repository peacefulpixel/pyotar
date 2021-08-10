package org.example.corp.engine.entity;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Sprite;

public abstract class GuiEntity extends RenderableEntity {
    public GuiEntity(Sprite sprite) throws EngineException {
        super(sprite);
    }
}
