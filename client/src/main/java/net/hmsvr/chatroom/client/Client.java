package net.hmsvr.chatroom.client;

import net.hmsvr.chatroom.client.event.EventDispatcher;
import net.hmsvr.chatroom.protocol.packet.ExitPacket;
import net.hmsvr.chatroom.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

/**
 * The client class.
 * It listens for input and communicates with the server using packets.
 */
public final class Client implements AutoCloseable {

    private final Socket socket;
    private final EventDispatcher dispatcher;
    private final InputThread inputThread;
    private final ListenThread listenThread;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private volatile boolean connected;

    private Client(int port) throws IOException {
        this.socket = new Socket((String) null, port);
        this.dispatcher = new EventDispatcher();
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.inputThread = new InputThread(this, dispatcher);
        this.listenThread = new ListenThread(this, in, dispatcher);
        this.connected = true;
    }

    /**
     * Gets the connected state of this client.
     * @return true if the client is connected, otherwise false
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sends a packet to the server.
     * @param packet the packet
     */
    public void sendPacket(@NotNull Packet packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (SocketException ignored) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void start() {
        listenThread.start();
        inputThread.start();
        while (connected) {
            dispatcher.pollEvents();
        }
    }

    /**
     * Disconnects this client from the server.
     */
    @Override
    public void close() throws Exception {
        if (!connected) return;
        sendPacket(new ExitPacket());
        connected = false;
        System.in.close();
        socket.close();
        listenThread.join();
        inputThread.join();
    }

    /**
     * The program entry point. The client is initialized here.
     */
    public static void main(String[] args) {
        try (Client client = new Client(3000)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> { // In case of SIGINT signal (CTRL + C)
                try {
                    client.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
            client.start();
        } catch (ConnectException e) {
            System.err.println("Failed to connect: No server found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
