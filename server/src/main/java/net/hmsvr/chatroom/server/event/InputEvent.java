package net.hmsvr.chatroom.server.event;

import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import net.hmsvr.chatroom.server.Server;
import org.jetbrains.annotations.NotNull;

/**
 * This event is dispatched when the server reads a string from the standard input stream.
 */
public final class InputEvent extends Event {

    private final String string;
    private final Server server;

    /**
     * Constructs a new InputEvent instance.
     * @param server the server
     * @param string the inputted string
     */
    public InputEvent(@NotNull String string, @NotNull Server server) {
        this.string = string;
        this.server = server;
    }

    /**
     * Broadcasts a {@link MessagePacket} containing the inputted string to all clients connected to the server.
     */
    @Override
    public void execute() {
        server.broadcast("Server: " + string);
    }
}
