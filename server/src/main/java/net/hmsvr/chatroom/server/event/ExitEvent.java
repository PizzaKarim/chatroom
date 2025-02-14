package net.hmsvr.chatroom.server.event;

import net.hmsvr.chatroom.server.Server;
import org.jetbrains.annotations.NotNull;

/**
 * This event is dispatched when the server closes.
 */
public final class ExitEvent extends Event {

    private final Server server;

    /**
     * Constructs a new ExitEvent instance.
     * @param server the closing server
     */
    public ExitEvent(@NotNull Server server) {
        this.server = server;
    }

    /**
     * Closes the server.
     */
    @Override
    public void execute() {
        try {
            server.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
