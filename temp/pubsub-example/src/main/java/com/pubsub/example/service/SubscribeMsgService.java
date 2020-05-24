package com.pubsub.example.service;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.pubsub.example.constants.Constants;

public class SubscribeMsgService {



  static class MessageReceiverExample implements MessageReceiver {
    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
      System.out.println(
          "Message Id: " + message.getMessageId() + " Data: " + message.getData().toStringUtf8());
      consumer.ack();
    }
  }

  /** Receive messages over a subscription. */
  public void receiveMsg() throws Exception {
    // set subscriber id, eg. my-sub

    ProjectSubscriptionName subscriptionName =
        ProjectSubscriptionName.of(Constants.PROJECT_ID, Constants.SUBSCRIPTION_ID);
    Subscriber subscriber = null;
    try {
      // create a subscriber bound to the asynchronous message receiver
      subscriber = Subscriber.newBuilder(subscriptionName, new MessageReceiverExample()).build();
      subscriber.startAsync().awaitRunning();
      // Allow the subscriber to run indefinitely unless an unrecoverable error occurs.
      subscriber.awaitTerminated();
    } catch (IllegalStateException e) {
      System.out.println("Subscriber unexpectedly stopped: " + e);
    }
  }
}

