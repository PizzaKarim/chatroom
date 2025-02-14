package net.hmsvr.chatroom.client;

import net.hmsvr.chatroom.client.event.EventDispatcher;
import net.hmsvr.chatroom.client.event.PacketEvent;
import net.hmsvr.chatroom.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

/**
 * A thread that listens for incoming packets and
 * forwards them to the {@link EventDispatcher} as a new {@link PacketEvent}.
 */
public final class ListenThread extends Thread {

    public ListenThread(@NotNull Client client, @NotNull ObjectInputStream in, @NotNull EventDispatcher dispatcher) {
        super(() -> {
            while (client.isConnected()) {
                try {
                    Object object = in.readObject();
                    if (!(object instanceof Packet packet)) throw new RuntimeException("Unrecognized message");
                    dispatcher.add(new PacketEvent(client, packet, dispatcher));
                } catch (EOFException ignored) {
                } catch (SocketException e) {
                    break;
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
