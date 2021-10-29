package org.example.corp.engine.graphics.font;

import org.example.corp.engine.exception.FontParsingException;

public interface FontParser<T extends Font> {
    T parse() throws FontParsingException;
}
