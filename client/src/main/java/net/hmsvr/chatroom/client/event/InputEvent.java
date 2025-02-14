package net.hmsvr.chatroom.client.event;

import net.hmsvr.chatroom.client.Client;
import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import org.jetbrains.annotations.NotNull;

/**
 * This event is dispatched when the client reads a string from the standard input stream.
 */
public final class InputEvent extends Event {

    private final Client client;
    private final String string;

    /**
     * Constructs a new InputEvent instance.
     * @param client the client
     * @param string the inputted string
     */
    public InputEvent(@NotNull Client client, @NotNull String string) {
        this.client = client;
        this.string = string;
    }

    /**
     * Sends a {@link MessagePacket} to the server containing the inputted string.
     */
    @Override
    public void execute() {
        client.sendPacket(new MessagePacket(string));
    }
}
