package com.sia.client.simulator.filter;

public class RejectFilter extends MesgFilterType {
    @Override
    public boolean test(String text) {
        return false;
    }
}
