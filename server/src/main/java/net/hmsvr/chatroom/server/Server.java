package net.hmsvr.chatroom.server;

import net.hmsvr.chatroom.server.event.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

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

    public boolean isRunning() {
        return running;
    }

    public void connect(@NotNull Client client) {
        clients.add(client);
        client.start();
    }

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

    @Override
    public void close() throws Exception {
        if (!running) return;
        for (Client client : clients) client.close();
        running = false;
        socket.close();
        inputThread.join();
        clientThread.join();
    }

    public static void main(String[] args) {
        try (Server server = new Server(3000)) {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
