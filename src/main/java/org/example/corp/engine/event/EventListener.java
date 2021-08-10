package org.example.corp.engine.event;

public interface EventListener <T extends Event> {
    void handle(T event);
}
