package com.sia.client.simulator.filter;

public class NoneFilter extends MesgFilterType {
    @Override
    public boolean test(String text) {
        return true;
    }
}
