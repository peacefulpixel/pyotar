package org.example.corp.engine.res;

import org.example.corp.engine.FinalObject;
import org.example.corp.engine.exception.ResourceInitializationException;
import org.example.corp.engine.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class BitmapFontResource extends Resource {
    private final FinalObject<String> fontSource = new FinalObject<>();
    private final FinalObject<Path> absolutePathOfFontDir = new FinalObject<>();

    @Override
    public void load(File file) throws ResourceInitializationException {
        try {
            fontSource.set(FileUtils.readFileAsString(file));
        } catch (IOException e) {
            throw new ResourceInitializationException("Unable to read font source file", e);
        }

        absolutePathOfFontDir.set(file.toPath().getParent());
        successfullyLoaded = true;
    }

    @Override
    public void sample() {
        fontSource.set("");
        absolutePathOfFontDir.set(FileSystems.getDefault().getPath("/"));
    }

    public String getFontSource() {
        return fontSource.get();
    }

    public Path getAbsolutePathOfFontDir() {
        return absolutePathOfFontDir.get();
    }
}
