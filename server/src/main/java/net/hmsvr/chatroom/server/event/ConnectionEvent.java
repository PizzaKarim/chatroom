package net.hmsvr.chatroom.server.event;

import net.hmsvr.chatroom.server.Client;
import net.hmsvr.chatroom.server.Server;
import org.jetbrains.annotations.NotNull;

/**
 * This event is dispatched when a client connects to the server.
 */
public final class ConnectionEvent extends Event {

    private final Server server;
    private final Client client;

    /**
     * Constructs a new ConnectionEvent instance.
     * @param server the server
     * @param client the connecting client
     */
    public ConnectionEvent(@NotNull Server server, @NotNull Client client) {
        this.server = server;
        this.client = client;
    }

    /**
     * Connects the client to the server and broadcasts a message to all connected clients.
     */
    @Override
    public void execute() {
        server.broadcast(client.getName() + " connected");
        server.connect(client);
    }
}
