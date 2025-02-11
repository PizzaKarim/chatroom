package net.hmsvr.chatroom.client.event;

import net.hmsvr.chatroom.client.Client;
import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import org.jetbrains.annotations.NotNull;

public final class InputEvent extends Event {

    private final Client client;
    private final String string;

    public InputEvent(@NotNull Client client, @NotNull String string) {
        this.client = client;
        this.string = string;
    }

    @Override
    public void execute() {
        client.sendPacket(new MessagePacket(string));
    }
}
