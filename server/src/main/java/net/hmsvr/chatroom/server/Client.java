package net.hmsvr.chatroom.server;

import net.hmsvr.chatroom.protocol.packet.Packet;
import net.hmsvr.chatroom.server.event.EventDispatcher;
import net.hmsvr.chatroom.server.event.PacketEvent;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * A class representing a client connection on the server.
 */
public final class Client implements AutoCloseable {

    private final Socket socket;
    private final PacketThread packetThread;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private volatile boolean connected;

    private String name;

    /**
     * Constructs a new client instance.
     * @param socket the client socket
     * @param server the server instance
     * @param dispatcher the server event dispatcher
     */
    public Client(@NotNull Socket socket, @NotNull Server server, @NotNull EventDispatcher dispatcher) {
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.packetThread = new PacketThread(server, dispatcher);
        this.connected = true;
        this.name = Integer.toString(socket.getPort());
    }

    /**
     * Gets the client name. Initially, this will be the remote port.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sends a packet to this client.
     * @param packet the packet
     */
    public void sendPacket(@NotNull Packet packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (SocketException e) {
            if (e.getMessage().equals("Socket closed")) return;
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Start listening for packets from this client.
     */
    public void start() {
        packetThread.start();
    }

    /**
     * Disconnects the client from the server
     */
    @Override
    public void close() throws Exception {
        if (!connected) return;
        connected = false;
        socket.close();
        packetThread.join();
    }

    /**
     * A thread that listens for incoming packets and
     * forwards them to the {@link EventDispatcher} as a new {@link PacketEvent}.
     */
    private final class PacketThread extends Thread {

        private PacketThread(@NotNull Server server, @NotNull EventDispatcher dispatcher) {
            super(() -> {
                while (connected) {
                    try {
                        Object object = in.readObject();
                        // TODO: Don't close server; just throw an error and disconnect the client
                        if (!(object instanceof Packet)) throw new RuntimeException("Unrecognized message");
                        dispatcher.add(new PacketEvent(server, Client.this, (Packet) object));
                    } catch (EOFException ignored) { // End of stream
                    } catch (SocketException e) { // Socket closed after first read because it is loaded into the buffer
                        connected = false;
                        break;
                    } catch (StreamCorruptedException e) {
                        connected = false;
                        break;
                    } catch (ClassNotFoundException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
