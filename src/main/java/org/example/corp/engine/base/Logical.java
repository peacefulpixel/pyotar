package org.example.corp.engine.base;

/**
 * By design, these methods never shouldn't be invoked outside of engine code, but you may do that if you're sure you
 * understand the risks.
 */
public interface Logical {
    void init();
    void loop();
}
