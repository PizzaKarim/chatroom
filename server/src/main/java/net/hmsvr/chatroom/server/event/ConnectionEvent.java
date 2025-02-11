package net.hmsvr.chatroom.server.event;

import net.hmsvr.chatroom.server.Client;
import net.hmsvr.chatroom.server.Server;
import org.jetbrains.annotations.NotNull;

public final class ConnectionEvent extends Event {

    private final Server server;
    private final Client client;

    public ConnectionEvent(@NotNull Server server, @NotNull Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void execute() {
        server.connect(client);
        // TODO: Change to something like a 'broadcast' call
        System.out.println(client.getName() + " connected");
    }
}
