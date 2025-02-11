package net.hmsvr.chatroom.server;

import net.hmsvr.chatroom.protocol.packet.Packet;
import net.hmsvr.chatroom.server.event.EventDispatcher;
import net.hmsvr.chatroom.server.event.PacketEvent;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public final class Client implements AutoCloseable {

    private final Socket socket;
    private final PacketThread packetThread;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private volatile boolean connected;

    private String name;

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

    public String getName() {
        return name;
    }

    public void start() {
        packetThread.start();
    }

    @Override
    public void close() throws Exception {
        if (!connected) return;
        connected = false;
        socket.close();
        packetThread.join();
    }

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
                        // TODO: Test the below and remove if unnecessary (rename 'e' to 'ignored' if so)
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
