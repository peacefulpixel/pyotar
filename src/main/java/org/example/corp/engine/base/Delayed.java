package org.example.corp.engine.base;

import java.lang.annotation.*;

/**
 * Means that entire method or part of it might be executed not instantly, corresponding to engine lifecycle.
 * For example these methods might produce some task which will be added to queue that getting polled and executed at
 * the end of frame rendering.
 */
@Documented
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface Delayed {
}
