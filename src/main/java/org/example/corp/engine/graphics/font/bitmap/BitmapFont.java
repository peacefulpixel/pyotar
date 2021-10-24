package org.example.corp.engine.graphics.font.bitmap;

import org.example.corp.engine.graphics.font.Font;
import org.example.corp.engine.graphics.font.bitmap.map.FontRoot;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Logger;

public class BitmapFont extends Font {
    private static final Logger logger = LoggerUtils.getLogger(BitmapFont.class);

    private FontRoot fontRoot;
    private Image[] images;

    public BitmapFont(FontRoot fontRoot, Image... images) {
        this.fontRoot = fontRoot;
        this.images = images;

        logger.fine("Font was created: " + fontRoot.info.face + " with " + images.length + " pages");
    }
}
