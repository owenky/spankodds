package com.sia.client.simulator.filter;

import com.sia.client.simulator.OngoingGameMessages;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MesgTypeFilter extends MesgFilterType {

    private List<OngoingGameMessages.MessageType> allowedMesgTypes;
    @Override
    public boolean test(String content, OngoingGameMessages.MessageType mesgType, LocalDateTime timeStamp) {
        return getAllowedMesgTypes().contains(mesgType);
    }
    private List<OngoingGameMessages.MessageType> getAllowedMesgTypes(){
        if ( null== allowedMesgTypes) {
            allowedMesgTypes = Arrays.stream(getConditions()).map(OngoingGameMessages.MessageType::valueOf).collect(Collectors.toList());
        }
        return allowedMesgTypes;
    }
}
