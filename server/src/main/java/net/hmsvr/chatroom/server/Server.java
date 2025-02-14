package net.hmsvr.chatroom.server;

import net.hmsvr.chatroom.protocol.packet.KickPacket;
import net.hmsvr.chatroom.protocol.packet.MessagePacket;
import net.hmsvr.chatroom.protocol.packet.Packet;
import net.hmsvr.chatroom.server.event.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

/**
 * The server class.
 * It listens for input and manages client connections, sending packets to communicate with them.
 */
public final class Server implements AutoCloseable {

    private final ServerSocket socket;
    private final EventDispatcher dispatcher;
    private final InputThread inputThread;
    private final ClientThread clientThread;
    private final Set<Client> clients;

    private volatile boolean running;

    private Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.dispatcher = new EventDispatcher();
        this.inputThread = new InputThread(this, dispatcher);
        this.clientThread = new ClientThread(this, socket, dispatcher);
        this.clients = new HashSet<>();
        this.running = true;
    }

    /**
     * A getter for the running state of the server.
     * @return true if the server is running, otherwise false
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sends a message packet to all connected clients.
     * @param message the message
     */
    public void broadcast(@NotNull String message) {
        System.out.println(message);
        MessagePacket packet = new MessagePacket(message);
        for (Client client : clients) client.sendPacket(packet);
    }

    /**
     * Connects a client, adding them to the list of connected clients.
     * @param client the client to connect
     */
    public void connect(@NotNull Client client) {
        clients.add(client);
        client.start();
    }

    /**
     * Disconnects a client, removing them from the list of connected clients.
     * @param client the client to disconnect
     */
    public void disconnect(@NotNull Client client) {
        clients.remove(client);
        try {
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void start() {
        clientThread.start();
        inputThread.start();
        while (running) {
            dispatcher.pollEvents();
        }
    }

    /**
     * Closes the server, releasing all resources.
     */
    @Override
    public void close() throws Exception {
        if (!running) return;
        for (Client client : clients) {
            client.sendPacket(new KickPacket("Server closed"));
            client.close();
        }
        running = false;
        socket.close();
        inputThread.join();
        clientThread.join();
    }

    /**
     * The program entry point. The server is initialized here.
     */
    public static void main(String[] args) {
        try (Server server = new Server(3000)) {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
