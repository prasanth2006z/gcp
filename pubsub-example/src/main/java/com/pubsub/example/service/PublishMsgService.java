package com.pubsub.example.service;


import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.pubsub.example.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class PublishMsgService {
    /**
     * Publish messages to a topic.
     *
     * @param message topic name, number of messages
     */
    public void publishMsg(String message) throws Exception {

        ProjectTopicName topicName = ProjectTopicName.of(Constants.PROJECT_ID, Constants.TOPIC_ID);
        Publisher publisher = null;
        List<ApiFuture<String>> futures = new ArrayList<>();

        try {
            publisher = Publisher.newBuilder(topicName).build();
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);
        } finally {
            // Wait on any pending requests
            List<String> messageIds = ApiFutures.allAsList(futures).get();
            for (String messageId : messageIds) {
                System.out.println(messageId);
            }

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
            }
        }
    }
}

