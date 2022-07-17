package com.sia.client.simulator.filter;

public abstract class MesgFilterType implements MessageFilterFactory.MesgFilter {

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
