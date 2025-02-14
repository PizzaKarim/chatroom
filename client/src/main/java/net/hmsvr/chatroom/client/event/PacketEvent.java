package net.hmsvr.chatroom.client.event;

import net.hmsvr.chatroom.client.Client;
import net.hmsvr.chatroom.protocol.packet.KickPacket;
import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import net.hmsvr.chatroom.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

/**
 * This event is dispatched when the client receives a packet from the server.
 */
public final class PacketEvent extends Event {

    private final Client client;
    private final Packet packet;
    private final EventDispatcher dispatcher;

    /**
     * Constructs a new PacketEvent instance.
     * @param client the client
     * @param packet the packet
     * @param dispatcher the event dispatcher
     */
    public PacketEvent(@NotNull Client client, @NotNull Packet packet, @NotNull EventDispatcher dispatcher) {
        this.client = client;
        this.packet = packet;
        this.dispatcher = dispatcher;
    }

    /**
     * Processes the packet based on the packet type.
     */
    @Override
    public void execute() {
        switch (packet.getType()) {
            case MESSAGE -> System.out.println(((MessagePacket) packet).getMessage());
            case KICK -> {
                String reason = ((KickPacket) packet).getReason();
                if (reason != null) {
                    System.out.println("You have been kicked: " + reason);
                } else {
                    System.out.println("You have been kicked.");
                }
                System.out.println("You may have to press CTRL + C to close this instance.");
                dispatcher.add(new ExitEvent(client));
            }
            default -> System.out.println("Server sent a packet of type " + packet.getType().name() + ": " + packet);
        }
    }
}
