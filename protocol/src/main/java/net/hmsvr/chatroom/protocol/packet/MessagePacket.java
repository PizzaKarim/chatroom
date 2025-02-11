package net.hmsvr.chatroom.protocol.packet;

import org.jetbrains.annotations.NotNull;

public final class MessagePacket extends Packet {

    private final String message;

    public MessagePacket(@NotNull String message) {
        super(Type.MESSAGE);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
