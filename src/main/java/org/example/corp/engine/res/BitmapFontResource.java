package org.example.corp.engine.res;

import org.example.corp.engine.FinalObject;
import org.example.corp.engine.exception.ResourceInitializationException;
import org.example.corp.engine.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class BitmapFontResource extends Resource {
    private final FinalObject<File> fontSource = new FinalObject<>();
    private final FinalObject<Path> absolutePathOfFontDir = new FinalObject<>();

    @Override
    public void load(File file) throws ResourceInitializationException {
//        try {
//            fontSource.set(FileUtils.readFileAsString(file));
//        } catch (IOException e) {
//            throw new ResourceInitializationException("Unable to read font source file", e);
//        }

        fontSource.set(file);
        absolutePathOfFontDir.set(file.toPath().getParent());
        successfullyLoaded = true;
    }

    @Override
    public void sample() {
        fontSource.set(new File("/"));
        absolutePathOfFontDir.set(FileSystems.getDefault().getPath("/"));
    }

    public File getFontSource() {
        return fontSource.get();
    }

    public Path getAbsolutePathOfFontDir() {
        return absolutePathOfFontDir.get();
    }
}
