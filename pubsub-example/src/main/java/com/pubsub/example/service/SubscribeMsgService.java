package com.pubsub.example.service;

import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.pubsub.example.constants.Constants;

public class SubscribeMsgService {
  ProjectSubscriptionName subscriptionName =
          ProjectSubscriptionName.of(Constants.PROJECT_ID, Constants.SUBSCRIPTION_ID);
  Subscriber subscriber = null;
    /**
     * Receive messages over a subscription.
     */
    public void receiveMsg(SubscribeMsgReceiveService subscribeMsgReceiveService) throws Exception {
        // set subscriber id, eg. my-sub
      try {
            // create a subscriber bound to the asynchronous message receiver
            subscriber = Subscriber.newBuilder(subscriptionName, subscribeMsgReceiveService).build();
            subscriber.addListener(new Subscriber.Listener() {
                @Override
                public void failed(Subscriber.State from, Throwable failure) {
                    System.err.println(failure);
                }
            }, MoreExecutors.directExecutor());
            subscriber.startAsync().awaitRunning();
            Thread.sleep(30000);
            // Allow the subscriber to run indefinitely unless an unrecoverable error occurs.
            //subscriber.awaitTerminated();
        } catch (IllegalStateException e) {
            System.out.println("Subscriber unexpectedly stopped: " + e);
        } finally {
            if (subscriber != null) {
                subscriber.stopAsync().awaitTerminated();
            }
        }
    }
}

