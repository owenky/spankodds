package com.sia.client.model;

import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;

import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface ColumnHeaderProvider extends Supplier<ColumnHeaderProperty> {


    ///////////////////////////////////////////////////////////////
    class ColumnHeaderProperty {
        public final Color headerBackground;
        public final Color headerForeground;
        public final Font headerFont;
        public final int columnHeaderHeight;
        public final Map<Integer,Object> rowIndexToHeadValueMap;

        public ColumnHeaderProperty(Color headerBackground, Color headerForeground, Font headerFont, int columnHeaderHeight,Map<Integer,Object> rowIndexToHeadValueMap) {
            this.headerBackground = headerBackground;
            this.headerForeground = headerForeground;
            this.headerFont = headerFont;
            this.columnHeaderHeight = columnHeaderHeight;
            Map<Integer, Object> clonedMap = new HashMap<>(rowIndexToHeadValueMap);
            this.rowIndexToHeadValueMap = Collections.unmodifiableMap(clonedMap);
        }
    }
}
