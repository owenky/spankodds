package com.sia.client.ui;

import javafx.beans.property.*;

public class StockQuoteOL {
 
    private StringProperty symbol; //   Name of the company
    private StringProperty name; // Name of the company
    private DoubleProperty lastPrice;// The last price of the company's stock
 
    public StockQuoteOL(String symbol, String name, Double lastPrice) {
        super();
        this.symbol = new SimpleStringProperty(symbol);
        this.name = new SimpleStringProperty(name);
        this.lastPrice = new SimpleDoubleProperty(lastPrice);
    }
 
    public StringProperty getSymbol() {
        return symbol;
    }
 
    public void setSymbol(StringProperty symbol) {
        this.symbol = symbol;
    }
 
    public StringProperty getName() {
        return name;
    }
 
    public void setName(StringProperty name) {
        this.name = name;
    }
 
    public DoubleProperty getLastPrice() {
        return lastPrice;
    }
 
    public void setLastPrice(DoubleProperty lastPrice) {
        this.lastPrice = lastPrice;
    }
 
}

/*
id
public final StringProperty idProperty
The id of this Node. This simple string identifier is useful for finding a specific Node within the scene graph. While the id of a Node should be unique within the scene graph, this uniqueness is not enforced. This is analogous to the "id" attribute on an HTML element (CSS ID Specification).
For example, if a Node is given the id of "myId", then the lookup method can be used to find this node as follows: scene.lookup("#myId");.

Default value:
null
See Also:
getId(), setId(String)
*/