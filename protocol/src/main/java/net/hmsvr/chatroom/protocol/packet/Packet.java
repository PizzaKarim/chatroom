package net.hmsvr.chatroom.protocol.packet;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * An abstract class inherited by all Packet classes.
 * A packet is some data sent between client and server.
 */
public abstract class Packet implements Serializable {

    protected final Type type;

    /**
     * The base constructor for any packet
     * @param type the packet type
     */
    protected Packet(@NotNull Type type) {
        this.type = type;
    }

    /**
     * A getter for the packet type.
     * @return the packet type
     */
    public final @NotNull Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type.name() + "]";
    }

    /**
     * An enum class of packet types.
     */
    public enum Type {

        /**
         * The exit packet is sent when a client disconnects from the server.
         * @see ExitPacket
         */
        EXIT(AllowedDispatcher.CLIENT),

        /**
         * The message packet is sent when either the client or server sends data to the other.
         * @see MessagePacket
         */
        MESSAGE(AllowedDispatcher.ANY),

        /**
         * The kick packet is sent when the server disconnects a client.
         * @see KickPacket
         */
        KICK(AllowedDispatcher.SERVER);

        private final AllowedDispatcher dispatcher;

        Type(AllowedDispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        /**
         * A getter for the allowed dispatcher; who can send the packet.
         * @return the allowed dispatcher
         */
        public AllowedDispatcher getAllowedDispatcher() {
            return dispatcher;
        }
    }
}
