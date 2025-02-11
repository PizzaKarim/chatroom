package net.hmsvr.chatroom.server;

import net.hmsvr.chatroom.server.event.ConnectionEvent;
import net.hmsvr.chatroom.server.event.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public final class ClientThread extends Thread {

    public ClientThread(@NotNull Server server, @NotNull ServerSocket serverSocket, @NotNull EventDispatcher dispatcher) {
        super(() -> {
            while (server.isRunning()) {
                try {
                    Socket socket = serverSocket.accept();
                    Client client = new Client(socket, server, dispatcher);
                    dispatcher.add(new ConnectionEvent(server, client));
                } catch (SocketException e) {
                    if (e.getMessage().equals("Socket closed")) break; // ServerSocket closed
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
