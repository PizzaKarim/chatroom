package net.hmsvr.chatroom.protocol.packet;

import org.jetbrains.annotations.Nullable;

/**
 * A packet indicating that the Server has kicked a client.
 */
public final class KickPacket extends Packet {

    private final String reason;

    /**
     * Constructs a new {@link KickPacket}, optionally with a reason.
     * @param reason the kick reason
     */
    public KickPacket(@Nullable String reason) {
        super(Type.KICK);
        this.reason = reason;
    }

    /**
     * A getter for the kick reason.
     * @return the kick reason
     */
    public @Nullable String getReason() {
        return reason;
    }
}
