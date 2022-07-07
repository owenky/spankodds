package com.sia.client.simulator.filter;

import java.util.function.Predicate;

public abstract class MesgFilterType implements Predicate<String> {

    private String [] conditions;

    public String [] getConditions() {
        return conditions;
    }

    public void setCondition(String conditionStr) {
        if ( null == conditionStr) {
            conditions = new String [0];
        } else {
            conditions = conditionStr.split(",");
        }
    }
}
