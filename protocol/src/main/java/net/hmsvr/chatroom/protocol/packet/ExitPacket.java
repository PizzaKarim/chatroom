package net.hmsvr.chatroom.protocol.packet;

/**
 * A packet indicating that the Client has disconnected.
 */
public final class ExitPacket extends Packet {

    /**
     * Constructs a new {@link ExitPacket}.
     */
    public ExitPacket() {
        super(Type.EXIT);
    }
}
