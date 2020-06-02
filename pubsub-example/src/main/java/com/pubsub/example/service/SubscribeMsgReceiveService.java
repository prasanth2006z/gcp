package com.pubsub.example.service;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

public class SubscribeMsgReceiveService implements MessageReceiver {

    private String subscriberMsgId;
    private String subscriberMsgData;

    public String getSubscriberMsgId() {
        return subscriberMsgId;
    }

    public void setSubscriberMsgId(String subscriberMsgId) {
        this.subscriberMsgId = subscriberMsgId;
    }

    public String getSubscriberMsgData() {
        return subscriberMsgData;
    }

    public void setSubscriberMsgData(String subscriberMsgData) {
        this.subscriberMsgData = subscriberMsgData;
    }

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        setSubscriberMsgId(message.getMessageId());
        setSubscriberMsgData(message.getData().toStringUtf8());
//        System.out.println(
//                "Message Id: " + message.getMessageId() + " Data: " + message.getData().toStringUtf8());
        consumer.ack();
    }
}
