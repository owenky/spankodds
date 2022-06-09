package com.sia.client.model;

public interface  AccessableToGame<T extends KeyedObject> {

    T getGame(int rowModelIndex);
}
