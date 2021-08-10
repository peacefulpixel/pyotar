package org.example.corp.engine.event;

import java.util.*;

public class EventManager {

    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public static <T extends Event> void addEventListener(Class<T> clazz, EventListener<T> listener) {
        List<EventListener> eventListeners;
        if ((eventListeners = listeners.get(clazz)) == null) {
            eventListeners = new LinkedList<>();
            listeners.put(clazz, eventListeners);
        }

        eventListeners.add(listener);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void provideEvent(Event event) {
        List<EventListener> eventListeners;
        if ((eventListeners = listeners.get(event.getClass())) != null) {
            for (EventListener listener : eventListeners) {
                listener.handle(event);
            }
        }
    }
}
