package com.sia.client.simulator.filter;

import com.sia.client.simulator.OngoingGameMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompositeFilter extends MesgFilterType {

    private final List<MesgFilterType> filterList = new ArrayList<>();
    @Override
    public boolean test(String content, OngoingGameMessages.MessageType mesgType, LocalDateTime timeStamp) {
        return filterList.stream().allMatch(f-> f.test(content, mesgType, timeStamp));
    }
    public CompositeFilter addFilter(MesgFilterType filter) {
        filterList.add(filter);
        return this;
    }
}
