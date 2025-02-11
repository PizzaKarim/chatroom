package net.hmsvr.chatroom.protocol.packet;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class Packet implements Serializable {

    protected final Type type;

    protected Packet(@NotNull Type type) {
        this.type = type;
    }

    public final @NotNull Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type.name() + "]";
    }

    public enum Type {
        EXIT,
        MESSAGE
    }
}
