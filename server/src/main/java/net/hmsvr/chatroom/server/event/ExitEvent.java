package net.hmsvr.chatroom.server.event;

import net.hmsvr.chatroom.server.Server;
import org.jetbrains.annotations.NotNull;

public final class ExitEvent extends Event {

    private final Server server;

    public ExitEvent(@NotNull Server server) {
        this.server = server;
    }

    @Override
    public void execute() {
        try {
            server.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
