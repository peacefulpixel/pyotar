package org.example.corp.engine.util;

import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.*;

public class GLUtils {

    public static final int[] SQUARE_ELEMENTS_ARRAY = new int[] {
            0, 1, 2, 2, 3, 0,
    };

    public static final int VECTOR_SIZE_2D = 2;
    public static final int VECTOR_SIZE_3D = 3;

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

    /**
     * CPU vertex array elements mapping (like in glDrawElements).
     * Produces an array independent of elements being bound.
     * @param array vertex array dependent on elements
     * @param elements corresponding elements to the vertex array provided
     * @param vecSize amount of vertices per single element
     * @return element non-based vertex array
     */
    public static float[] mapVertexArrayOnElements(float[] array, int[] elements, int vecSize) {
        float[] newArray = new float[elements.length * vecSize];
        for (int elementIndex = 0; elementIndex < elements.length; elementIndex++) {
            System.arraycopy(array, elements[elementIndex] * vecSize,
                             newArray, elementIndex * vecSize, vecSize);
        }

        return newArray;
    }

    /**
     * Does the same as {@link GLUtils#mapVertexArrayOnElements(float[], int[], int)} but reverse
     * @param array element non-based vertex array
     * @param elements elements to base vertex array on
     * @param vecSize amount of vertices per single element
     * @return elements based array
     */
    public static float[] unmapVertexArrayFromElements(float[] array, int[] elements, int vecSize) {
        int newSize = 0;
        for (int index : elements) {
            if (index > newSize) newSize = index;
        } newSize++;

        float[] newArray = new float[newSize * vecSize];
        for (int arrIndex = 0; arrIndex < newArray.length - 1; arrIndex += vecSize) {
            int providedArrayIndex = 0;
            for (int index : elements) {
                if (index ==
                        (arrIndex == 0 ? arrIndex : arrIndex / vecSize))
                    break;
                providedArrayIndex++;
            }

            System.arraycopy(array, providedArrayIndex * vecSize, newArray, arrIndex, vecSize);
        }

        return newArray;
    }
}
