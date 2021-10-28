package org.example.corp.engine.graphics.font.bitmap;

import org.example.corp.engine.entity.TextEntity;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.graphics.Texture;
import org.example.corp.engine.graphics.font.Font;
import org.example.corp.engine.graphics.font.Glyph;
import org.example.corp.engine.graphics.font.bitmap.map.Char;
import org.example.corp.engine.graphics.font.bitmap.map.FontRoot;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.util.LoggerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BitmapFont implements Font {
    private static final Logger logger = LoggerUtils.getLogger(BitmapFont.class);

    private FontRoot fontRoot;
    private Map<String, Texture> textures = new HashMap<>();
    private Map<Float, Glyph> glyphs = new HashMap<>();

    public BitmapFont(FontRoot fontRoot, Map<String, Image> images) {
        this.fontRoot = fontRoot;

        images.forEach((k, v) -> textures.put(k, new Texture(v)));
        reloadGlyphs();

        logger.fine("Font was created: " + fontRoot.info.face + " with " + images.size() + " pages");
    }

    @Override
    public void reloadGlyphs() {
        glyphs.clear();

        for (Char char_ : fontRoot.chars) {
            try {
                glyphs.put(char_.id, new Glyph(textures.get(char_.page), char_.x, char_.y, char_.width, char_.height));
            } catch (EngineException e) {
                // That shouldn't happen so just logging an exception
                logger.log(Level.WARNING, "Unable to create glyph: " + char_.id, e);
            }
        }
    }

    @Override
    public Glyph getGlyph(int glyphId) {
        return glyphs.get((float) glyphId);
    }

    @Override
    public TextEntity text(String text) {
        return new TextEntity(this, text);
    }

    @Override
    public float getLineHeight() {
        return fontRoot.common.lineHeight;
    }

    @Override
    public float getRightPadding() {
        return 2.0f; //TODO Read from font file
    }
}
