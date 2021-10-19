package org.example.corp.engine.graphics.font.bitmap;

import org.example.corp.engine.exception.FontParsingException;
import org.example.corp.engine.graphics.font.FontParser;
import org.example.corp.engine.graphics.font.bitmap.map.*;
import org.example.corp.engine.res.BitmapFontResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class BitmapFontParser implements FontParser<BitmapFont> {

    private static final Class[] classesForMapping = new Class[] {
            FontRoot.class, Info.class, Common.class, Pages.class, Page.class, Chars.class, Char.class,
    };

    private BitmapFontResource fontResource;

    public BitmapFontParser(BitmapFontResource fontResource) {
        this.fontResource = fontResource;
    }

    private FontRoot serializedResource(BitmapFontResource fontResource) throws FontParsingException {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(classesForMapping);
        } catch (JAXBException e) {
            throw new FontParsingException("Unable to create JAXB context", e);
        }

        Unmarshaller unmarshaller;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new FontParsingException("Unable to create JAXB unmarshaller", e);
        }

        try {
            return (FontRoot) unmarshaller.unmarshal(fontResource.getFontSource());
        } catch (JAXBException e) {
            throw new FontParsingException("Unable to serialize the font", e);
        }
    }

    @Override
    public BitmapFont parse() throws FontParsingException {
        if (!fontResource.isSuccessfullyLoaded()) {
            throw new FontParsingException("Provided font resource wasn't successfully loaded");
        }

        FontRoot fontRoot = serializedResource(fontResource);
        return null;
    }
}
