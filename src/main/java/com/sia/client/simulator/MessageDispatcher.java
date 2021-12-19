package com.sia.client.simulator;

import com.sia.client.config.Utils;

import javax.jms.Message;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MessageDispatcher {

    private final Consumer<Message> messageProcessor;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final String name;

    public MessageDispatcher(Consumer<Message> messageProcessor,String name) {
        this.messageProcessor = messageProcessor;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void dispatch(Message message) {
        executor.execute(()->{
            if ( message instanceof LocalMessage) {
                LocalMessageLogger.localMessageTimeStamp.set( ((LocalMessage)message).getMessageTime());
            }
            Utils.log("consumer="+name+", "+message);
            messageProcessor.accept(message);
        });
    }
}
