package com.sia.client.simulator.filter;

import com.sia.client.simulator.OngoingGameMessages;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class MessageFilterFactory {

    private static final Map<String,String> typeMap = new HashMap<>();

    static {
        typeMap.put("NoneFilter",NoneFilter.class.getName());
        typeMap.put("SportFilter",SportFilter.class.getName());
        typeMap.put("KeywordFilter",KeywordFilter.class.getName());
        typeMap.put("RejectFilter",RejectFilter.class.getName());
        typeMap.put("MesgTypeFilter",MesgTypeFilter.class.getName());
    }
    public static MesgFilterType parse(String filterString) {
        CompositeFilter compositeFilter = new CompositeFilter();
        if ( null != filterString) {
            String[] parts = filterString.split("\\+");
            Arrays.stream(parts).map(MessageFilterFactory::parseSingleFilter)
                    .forEach(compositeFilter::addFilter);
        } else {
            compositeFilter.addFilter(new NoneFilter());
        }

        return compositeFilter;
    }
    private static MesgFilterType parseSingleFilter(String filterString) {
        if ( null == filterString || filterString.trim().isEmpty()) {
            filterString="NoneFilter:none";
        }
        String [] props = filterString.split(":");
        String err = null;
        MesgFilterType mesgFilterType = null;
        if ( 2 != props.length) {
            err = "Message filter string format -- MesgFilterType:Condition";
        } else {
            String filterTypeName = typeMap.get(props[0]);
            if ( null == filterTypeName) {
                err = "Message Filter Type '"+props[0]+"' is not in "+typeMap.keySet();
            } else {
                try {
                    mesgFilterType = (MesgFilterType)Class.forName(filterTypeName).newInstance();
                    mesgFilterType.setCondition(props[1]);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        if ( null != err) {
            System.err.println(err);
            System.exit(1);
        }
        return mesgFilterType;
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    public interface MesgFilter {
        boolean test(String content, OngoingGameMessages.MessageType mesgType, LocalDateTime timeStamp);
    }
}
