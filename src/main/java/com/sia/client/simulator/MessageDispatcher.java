package com.sia.client.simulator;

import javax.jms.Message;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MessageDispatcher {

    private final Consumer<Message> messageProcessor;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public MessageDispatcher(Consumer<Message> messageProcessor) {
        this.messageProcessor = messageProcessor;
    }
    public void dispatch(Message message) {
        executor.execute(()->messageProcessor.accept(message));
    }
}
