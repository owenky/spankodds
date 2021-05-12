package com.sia.client.model;

import javax.swing.table.TableCellRenderer;
import java.util.function.BiFunction;

public interface TableCellRendererProvider extends BiFunction<Integer, Integer, TableCellRenderer> {

}
