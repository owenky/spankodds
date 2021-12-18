package com.sia.client.simulator;

import java.time.LocalDateTime;

public abstract class LocalMessage {

    private final LocalDateTime messageTime;
    protected LocalMessage(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }
    public LocalDateTime getMessageTime() {
        return messageTime;
    }
}
