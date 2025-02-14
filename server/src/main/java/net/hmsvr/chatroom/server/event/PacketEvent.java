package net.hmsvr.chatroom.server.event;

import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import net.hmsvr.chatroom.protocol.packet.Packet;
import net.hmsvr.chatroom.server.Client;
import net.hmsvr.chatroom.server.Server;
import org.jetbrains.annotations.NotNull;

/**
 * This event is dispatched when the server receives a packet from a client.
 */
public final class PacketEvent extends Event {

    private final Server server;
    private final Client client;
    private final Packet packet;

    /**
     * Constructs a new PacketEvent instance.
     * @param server the server
     * @param client the client
     * @param packet the packet
     */
    public PacketEvent(@NotNull Server server, @NotNull Client client, @NotNull Packet packet) {
        this.server = server;
        this.client = client;
        this.packet = packet;
    }

    /**
     * Processes the packet based on the packet type.
     */
    @Override
    public void execute() {
        switch (packet.getType()) {
            case EXIT -> {
                server.disconnect(client);
                server.broadcast(client.getName() + " disconnected");
            }
            case MESSAGE -> {
                server.broadcast(client.getName() + ": " + ((MessagePacket) packet).getMessage());
            }
            default -> {
                System.out.println(client.getName() + " sent a packet of type " + packet.getType().name() + ": " + packet);
            }
        }
    }
}
