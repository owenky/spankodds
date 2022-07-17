package com.sia.client.simulator.filter;

import com.sia.client.simulator.OngoingGameMessages;

import java.time.LocalDateTime;

public class KeywordFilter extends MesgFilterType {

    @Override
    public boolean test(String content, OngoingGameMessages.MessageType mesgType, LocalDateTime timeStamp) {
        boolean status = false;
        for(String keyword: getConditions()) {
            if ( content.contains(keyword)) {
                status=true;
                break;
            }
        }
        return status;
    }
}
