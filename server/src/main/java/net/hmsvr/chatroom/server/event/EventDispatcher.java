package net.hmsvr.chatroom.server.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that processes events.
 */
public final class EventDispatcher {

    private final List<Event> events;

    /**
     * Constructs a new event dispatcher.
     */
    public EventDispatcher() {
        this.events = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adds an event to the pool.
     * @param event the event
     */
    public void add(@NotNull Event event) {
        events.add(event);
    }

    /**
     * Waits for incoming events, then processes them. This method is blocking until an event has been received.
     */
    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void pollEvents() {
        while (events.isEmpty()) Thread.onSpinWait();
        synchronized (events) {
            // Avoids ConcurrentModificationException by checking size rather than iterating directly
            for (int i = 0; i < events.size(); i++) events.get(i).execute();
            events.clear();
        }
    }
}
