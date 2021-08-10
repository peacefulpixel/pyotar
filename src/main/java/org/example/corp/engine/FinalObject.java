package org.example.corp.engine;

import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Logger;

/**
 * Class for final variables post-initialization.
 * It might be used in case, when you unable to initialize final fields in class in constructor, but they should be
 * initialized only once by design.
 *
 * When {@link FinalObject#set(Object)} method being called second time, it just ignores the new value and writes a
 * warning in log.
 * Also, {@link FinalObject#get()} will return null if variable was not initialized yet
 * @param <T>
 */
public class FinalObject<T> {

    public static final Logger logger = LoggerUtils.getLogger(FinalObject.class);

    private T value = null;
    private boolean isInitialized = false;

    public void set(T value) {
        if (isInitialized) {
            logger.warning("Attempt to set value to already initialized FinalObject. this.value=" + this.value
                    + ", value=" + value);
            return;
        }

        this.value = value;
        isInitialized = true;
    }

    public T get() {
        return value;
    }

    /**
     * @return is {@link FinalObject#set(Object)} was invoked or not
     */
    public boolean isInitialized() {
        return isInitialized;
    }
}
