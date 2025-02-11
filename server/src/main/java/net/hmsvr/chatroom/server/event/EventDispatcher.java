package net.hmsvr.chatroom.server.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class EventDispatcher {

    private final List<Event> events;

    public EventDispatcher() {
        this.events = Collections.synchronizedList(new ArrayList<>());
    }

    public void add(@NotNull Event event) {
        events.add(event);
    }

    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public void pollEvents() {
        while (events.isEmpty()) Thread.onSpinWait();
        synchronized (events) {
            Iterator<Event> it = events.iterator();
            while (it.hasNext()) {
                it.next().execute();
                it.remove();
            }
        }
    }
}
