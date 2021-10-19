package org.example.corp.engine.res;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.example.corp.engine.exception.EngineException;
import org.example.corp.engine.FinalObject;
import org.example.corp.engine.exception.ResourceInitializationException;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.IntFunction;

public class Image extends Resource {
    private final FinalObject<ByteBuffer> decodedImage = new FinalObject<>();
    private final FinalObject<Integer> width = new FinalObject<>();
    private final FinalObject<Integer> height = new FinalObject<>();

    @Override
    public void load(File file) throws ResourceInitializationException {
        final String absolutePath = file.getAbsolutePath();
        InputStream imageIS;
        try {
            imageIS = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ResourceInitializationException("Unable to read sprite image resource: " + absolutePath, e);
        }

        PNGDecoder decoder;
        try {
            decoder = new PNGDecoder(imageIS);
        } catch (IOException e) {
            throw new ResourceInitializationException("Unable to initialize PNGDecoder for image: " + absolutePath, e);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

        try {
            decoder.decode(byteBuffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            throw new ResourceInitializationException("Unable to decode image: " + absolutePath, e);
        }

        byteBuffer.flip();

        decodedImage.set(byteBuffer);
        width.set(decoder.getWidth());
        height.set(decoder.getHeight());
        successfullyLoaded = true;
    }

    @Override
    public void sample() {
        int size = 4 * 32 * 32;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size);
        int[] masks = {3584, 448, 56, 7};
        int[] shifting = {9, 6, 3, 0};
        Byte[] bytes = new Byte[size];
        Arrays.setAll(bytes, value -> {
            int channel = value % 4;
            return (byte) (((value & masks[channel]) >> shifting[channel]) * 36);
        });

        for (Byte bite : bytes)
            byteBuffer.put(bite);

        byteBuffer.flip();

        decodedImage.set(byteBuffer);
        width.set(32);
        height.set(32);
    }

    public ByteBuffer getDecodedImage() {
        return decodedImage.get();
    }

    public int getWidth() {
        return width.get();
    }

    public int getHeight() {
        return height.get();
    }
}
