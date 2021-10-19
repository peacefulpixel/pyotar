package org.example.corp.engine.graphics.font.bitmap;

import org.example.corp.engine.exception.FontParsingException;
import org.example.corp.engine.graphics.font.FontParser;
import org.example.corp.engine.graphics.font.bitmap.map.*;
import org.example.corp.engine.res.BitmapFontResource;
import org.example.corp.engine.res.Image;
import org.example.corp.engine.util.LoggerUtils;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

import static org.example.corp.engine.util.StringUtils.isStringNullOrEmpty;

public class BitmapFontParser implements FontParser<BitmapFont> {
    private static final Logger logger = LoggerUtils.getLogger(BitmapFontParser.class);
    private static final Map<String, Class> keyMappings = new HashMap<>();
    private static final Map<Class, TypeConverter> converterMappings = new HashMap<>();
    static {
        keyMappings.put("info", Info.class);
        keyMappings.put("common", Common.class);
        keyMappings.put("page", Page.class);
        keyMappings.put("char", Char.class);
        converterMappings.put(Float.class, Float::parseFloat);
        converterMappings.put(String.class, s -> s);
    }

    private String fontSource;
    private List<Image> images;
    private Path fontParentDirPath;

    public BitmapFontParser(BitmapFontResource fontResource) throws FontParsingException {

        if (!fontResource.isSuccessfullyLoaded()) {
            throw new FontParsingException("Provided font resource wasn't successfully loaded");
        }

        fontSource = fontResource.getFontSource();
        images = new ArrayList<>();
        fontParentDirPath = fontResource.getAbsolutePathOfFontDir();
    }

    private FontRoot createSampleFontRoot() {
        FontRoot fontRoot = new FontRoot();
        fontRoot.info = new Info();
        fontRoot.common = new Common();
        fontRoot.pages = new ArrayList<>();
        fontRoot.chars = new ArrayList<>();

        return fontRoot;
    }

    @SuppressWarnings("rawtypes")
    private FontRoot serializeSource() throws FontParsingException {
        FontRoot fontRoot = createSampleFontRoot();

        String[] sourceLines = fontSource.split("\n");
        for (String sourceLine : sourceLines) {
            Iterator<String> lineIterator = Arrays.stream(sourceLine.split(" ")).iterator();
            String command;
            if (!lineIterator.hasNext() || isStringNullOrEmpty(command = lineIterator.next())) {
                logger.warning("Invalid line in font source. Skipping");
                continue;
            }

            Class mappedClass = keyMappings.get(command);
            if (mappedClass == null && !command.equals("chars")) {
                logger.warning("Unrecognized keyword in bitmap font file: " + command + ". Skipping");
                continue;
            } else if (command.equals("chars")) continue;

            Object currentObject = null;
            if (command.equals("info"))   currentObject = fontRoot.info;
            if (command.equals("common")) currentObject = fontRoot.common;
            if (command.equals("page"))   currentObject = new Page();
            if (command.equals("char"))   currentObject = new Char();

            Map<String, String> parameters = new HashMap<>();
            while (lineIterator.hasNext()) {
                /*  There's could be a bug when parameter value have whitespaces in name and have quotes to handle that,
                    but we separating entire line by whitespace in the beginning of this code, so here we will parse it
                    incorrectly. Actually, it's not necessary to handle, but definitely that should be documented
                    somewhere (better to fix tho).
                 */
                String[] next = lineIterator.next().split("=");
                if (next.length != 2) {
                    logger.warning("Invalid parameter: " + Arrays.toString(next) + ". Skipping");
                    continue;
                }

                String key = next[0];
                String val = next[1];
                Field field;
                try {
                    field = mappedClass.getField(key);
                } catch (NoSuchFieldException e) {
                    logger.warning("Unknown parameter name: " + key + ". Skipping");
                    continue;
                }

                /* Converter for this field type should always exist and not requires to verify that existence, since
                   type and converter are both defined in internal engine code and not supposed to change externally.
                 */
                TypeConverter converter = converterMappings.get(field.getType());
                Object value = converter.convert(val);
                try {
                    field.set(currentObject, value);
                } catch (IllegalAccessException e) {
                    throw new FontParsingException("omg how the fuck that happened");
                }
            }

            if (command.equals("page")) fontRoot.pages.add((Page) currentObject);
            if (command.equals("char")) fontRoot.chars.add((Char) currentObject);
        }

        return fontRoot;
    }

    @Override
    public BitmapFont parse() throws FontParsingException {
        FontRoot fontRoot = serializeSource();
        return null;
    }

    private interface TypeConverter {
        Object convert(String string);
    }
}
