package org.example.corp.engine.util;

import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * Utility class for memory-safe GL buffers operations
 */
public class BufferUtils {

    public static void glBufferData(int target, int[] array, int usage) {
        if (array == null) return;
        IntBuffer buffer = memAllocInt(array.length);
        buffer.put(array).flip();
        GL15.glBufferData(target, buffer, usage);
        memFree(buffer);
    }

    public static void glBufferData(int target, float[] array, int usage) {
        if (array == null) return;
        FloatBuffer buffer = memAllocFloat(array.length);
        buffer.put(array).flip();
        GL15.glBufferData(target, buffer, usage);
        memFree(buffer);
    }
}
