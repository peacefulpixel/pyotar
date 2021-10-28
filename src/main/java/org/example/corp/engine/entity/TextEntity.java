package org.example.corp.engine.entity;

import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.font.Font;
import org.example.corp.engine.graphics.font.Glyph;
import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TextEntity extends GuiEntity {
    private static final Logger logger = LoggerUtils.getLogger(TextEntity.class);

    private Font font;
    private String text;
    private float x = 0.0f, y = 0.0f;
    private Glyph[] glyphs;

    public TextEntity(Font font, String text) {
        this.font = font;
        this.text = text;

        // Not the beautiful, but simple and fast
        int length = 0;
        for (char char_ : text.toCharArray()) {
            if (char_ != '\n') length++;
        }

        glyphs = new Glyph[length];

        int glyphsIndex = 0;
        int line = 0;
        for (char char_ : text.toCharArray()) {
            if (char_ == '\n') {
                line++;
                continue;
            }
            try {
                glyphs[glyphsIndex] = font.getGlyph(char_).cloneIt();
                glyphs[glyphsIndex].setLine(line);
                System.out.println("SET TO GLYPH " + glyphs[glyphsIndex]);
            } catch (EngineException e) {
                // Shouldn't happen
                logger.log(Level.WARNING, "Couldn't clone a glyph from a font", e);
            }

            glyphsIndex++;
        }

        System.out.println("" + glyphsIndex + " " + glyphs.length);

//        layer.addEntities(glyphs);
        glyphReposition();
    }

    private void glyphReposition() {
        int line = 0;
        float globalPadding = 0.0f;
        for (Glyph glyph : glyphs) {
            if (glyph.getLine() > line) {
                line = glyph.getLine();
                globalPadding = 0.0f;
            }
            glyph.setX(x + globalPadding);
            glyph.setY(y - line * font.getLineHeight());

            globalPadding += glyph.getWidth() + font.getRightPadding();
            glyph.setLayer(layer);
        }
    }

    @Override
    public void render() {
        for (Glyph glyph : glyphs) {
            if (glyph.layer == null) glyphReposition();
            glyph.render();
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        glyphReposition();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        glyphReposition();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        glyphReposition();
    }
}
