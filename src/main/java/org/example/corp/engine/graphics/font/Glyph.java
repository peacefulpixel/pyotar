package org.example.corp.engine.graphics.font;

import org.example.corp.engine.entity.SimpleSquareEntity;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;

public class Glyph extends SimpleSquareEntity {
    private final Texture texture;
    private final float texX, texY;
    private int line = 0;

    public Glyph(Texture texture, float texX, float texY, float width, float height) throws EngineException {
        super(texture);

        this.texture = texture;
        this.texX = texX;
        this.texY = texY;

        frame(texX, texY, width, height);
        setSize(width, height);
        setAxis(0.0f, 0.0f);
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Glyph cloneIt() throws EngineException {
        return new Glyph(texture, texX, texY, getWidth(), getHeight());
    }
}
