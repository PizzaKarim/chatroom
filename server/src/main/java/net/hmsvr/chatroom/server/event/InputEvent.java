package net.hmsvr.chatroom.server.event;

import org.jetbrains.annotations.NotNull;

public final class InputEvent extends Event {

    private final String string;

    public InputEvent(@NotNull String string) {
        this.string = string;
    }

    @Override
    public void execute() {
        System.out.println("Input: " + string);
    }
}
