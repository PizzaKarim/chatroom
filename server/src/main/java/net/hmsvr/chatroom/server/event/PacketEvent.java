package net.hmsvr.chatroom.server.event;

import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import net.hmsvr.chatroom.protocol.packet.Packet;
import net.hmsvr.chatroom.server.Client;
import net.hmsvr.chatroom.server.Server;
import org.jetbrains.annotations.NotNull;

public final class PacketEvent extends Event {

    private final Server server;
    private final Client client;
    private final Packet packet;

    public PacketEvent(@NotNull Server server, @NotNull Client client, @NotNull Packet packet) {
        this.server = server;
        this.client = client;
        this.packet = packet;
    }

    @Override
    public void execute() {
        switch (packet.getType()) {
            case EXIT -> {
                server.disconnect(client);
                // TODO: Change to something like a 'broadcast' call
                System.out.println(client.getName() + " disconnected");
            }
            case MESSAGE -> {
                // TODO: Change to something like a 'broadcast' call
                System.out.println(client.getName() + ": " + ((MessagePacket) packet).getMessage());
            }
            default -> {
                System.out.println(client.getName() + " sent a packet of type " + packet.getType().name() + ": " + packet);
            }
        }
    }
}
