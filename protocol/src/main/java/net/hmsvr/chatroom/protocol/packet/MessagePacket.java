package net.hmsvr.chatroom.protocol.packet;

import org.jetbrains.annotations.NotNull;

/**
 * A packet that contains a message for the receiver.
 */
public final class MessagePacket extends Packet {

    private final String message;

    /**
     * Constructs a new {@link MessagePacket}.
     * @param message the message
     */
    public MessagePacket(@NotNull String message) {
        super(Type.MESSAGE);
        this.message = message;
    }

    /**
     * A getter for the message.
     * @return the message
     */
    public @NotNull String getMessage() {
        return message;
    }
}
