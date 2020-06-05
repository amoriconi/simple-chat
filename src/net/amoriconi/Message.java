package net.amoriconi;

import java.io.Serializable;

public class Message implements Serializable {
    private final int id;
    private final String message;

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
