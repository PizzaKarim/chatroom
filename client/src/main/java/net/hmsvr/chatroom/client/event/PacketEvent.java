package net.hmsvr.chatroom.client.event;

import net.hmsvr.chatroom.client.Client;
import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import net.hmsvr.chatroom.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

public final class PacketEvent extends Event {

    private final Client client;
    private final Packet packet;

    public PacketEvent(@NotNull Client client, @NotNull Packet packet) {
        this.client = client;
        this.packet = packet;
    }

    @Override
    public void execute() {
        switch (packet.getType()) {
            case MESSAGE -> System.out.println(((MessagePacket) packet).getMessage());
            default -> System.out.println("Server sent a packet of type " + packet.getType().name() + ": " + packet);
        }
    }
}
