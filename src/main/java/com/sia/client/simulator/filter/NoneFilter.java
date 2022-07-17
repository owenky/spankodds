package com.sia.client.simulator.filter;

import com.sia.client.simulator.OngoingGameMessages;

import java.time.LocalDateTime;

public class NoneFilter extends MesgFilterType {
    @Override
    public boolean test(String content, OngoingGameMessages.MessageType mesgType, LocalDateTime timeStamp) {
        return true;
    }
}
