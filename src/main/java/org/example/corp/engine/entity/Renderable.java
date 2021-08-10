package org.example.corp.engine.entity;

/**
 * By design, these methods never shouldn't be invoked outside of engine code, but you may do that if you're sure you
 * understand the risks.
 */
public interface Renderable {
    void render();
}
