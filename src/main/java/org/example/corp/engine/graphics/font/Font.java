package org.example.corp.engine.graphics.font;

import org.example.corp.engine.entity.TextEntity;

public interface Font {
    void reloadGlyphs();
    Glyph getGlyph(int glyphId);
    TextEntity text(String text);
    float getLineHeight();
    float getRightPadding();
}
