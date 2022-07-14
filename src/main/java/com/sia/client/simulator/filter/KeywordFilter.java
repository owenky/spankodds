package com.sia.client.simulator.filter;

public class KeywordFilter extends MesgFilterType {

    @Override
    public boolean test(String text) {
        boolean status = false;
        for(String keyword: getConditions()) {
            if ( text.contains(keyword)) {
                status=true;
                break;
            }
        }
        return status;
    }
}
