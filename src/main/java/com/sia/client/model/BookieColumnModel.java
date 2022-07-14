package com.sia.client.model;

import javax.swing.table.TableColumn;

public interface BookieColumnModel {

    int size();
    TableColumn get(int index);
}
