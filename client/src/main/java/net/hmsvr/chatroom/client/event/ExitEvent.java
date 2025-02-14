package net.hmsvr.chatroom.client.event;

import net.hmsvr.chatroom.client.Client;
import org.jetbrains.annotations.NotNull;

import java.net.SocketException;

/**
 * This event is dispatched when the client disconnects from the server.
 */
public final class ExitEvent extends Event {

    private final Client client;

    /**
     * Constructs a new ExitEvent instance.
     * @param client the client to disconnect
     */
    public ExitEvent(@NotNull Client client) {
        this.client = client;
    }

    /**
     * Disconnects the client.
     */
    @Override
    public void execute() {
        try {
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
