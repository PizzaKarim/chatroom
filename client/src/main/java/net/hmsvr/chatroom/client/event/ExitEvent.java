package net.hmsvr.chatroom.client.event;

import net.hmsvr.chatroom.client.Client;
import org.jetbrains.annotations.NotNull;

public final class ExitEvent extends Event {

    private final Client client;

    public ExitEvent(@NotNull Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        try {
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
