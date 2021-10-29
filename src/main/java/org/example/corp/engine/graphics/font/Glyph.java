package org.example.corp.engine.graphics.font;

import org.example.corp.engine.entity.SimpleSquareEntity;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;
import org.example.corp.engine.graphics.font.bitmap.map.Char;

public class Glyph extends SimpleSquareEntity {
    private final Texture texture;
    private int line = 0;
    private Char char_; //TODO: Better to make immutable since this reference could be shared to other Glyph instances

    public Glyph(Texture texture, Char char_) throws EngineException {
        super(texture);

        this.texture = texture;
        this.char_ = char_;

        frame(char_.x, char_.y, char_.width, char_.height);
        setSize(char_.width, char_.height);
        setAxis(0.0f, 0.0f);
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public float getXAdvance() {
        return char_.xadvance;
    }

    public float getYOffset() {
        return char_.yoffset;
    }

    public Glyph cloneIt() throws EngineException {
        return new Glyph(texture, char_);
    }
}
