package com.sia.client.model;

import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;

import java.awt.Color;
import java.util.Set;
import java.util.function.Supplier;

public interface ColumnHeaderProvider extends Supplier<ColumnHeaderProperty> {


    ///////////////////////////////////////////////////////////////
    class ColumnHeaderProperty {
        public final Color haderBackground;
        public final int columnHeaderHeight;
        public final Set<Integer> columnHeaderIndexSet;

        public ColumnHeaderProperty(Color haderBackground,int columnHeaderHeight,Set<Integer> columnHeaderIndexSet) {
            this.haderBackground = haderBackground;
            this.columnHeaderHeight = columnHeaderHeight;
            this.columnHeaderIndexSet = columnHeaderIndexSet;
        }
    }
}
