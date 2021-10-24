package org.example.corp.engine.entity;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;

public class TextEntity extends GuiEntity {

    @Override
    public void render() {

    }

    public class GlyphEntity extends SimpleSquareEntity {

        public GlyphEntity(Texture texture) throws EngineException {
            super(texture);
        }
    }
}
