package com.pubsub.example.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.pubsub.example.model.Body;
import com.pubsub.example.service.PublishMsgService;
import com.pubsub.example.service.SubscribeMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Base64;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class PubSubController {

    private static final String TEMPLATE = "Hello, %s! You are visitor number %s";
    private final AtomicLong counter = new AtomicLong();
    private static final Logger logger = LoggerFactory.getLogger(PubSubController.class);
    private final Gson gson = new Gson();
    private final JsonParser jsonParser = new JsonParser();

    @RequestMapping("/publish")
    public String greeting(@RequestParam(value = "name", defaultValue = "World!!!") String name) throws Exception {
        logger.info("Logging INFO with Logback");
        logger.error("Logging ERROR with Logback");
        PublishMsgService pe = new PublishMsgService();
        pe.publishMsg("Hi Prasanth!!!");
        return String.format(TEMPLATE, name, counter.incrementAndGet());
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public ResponseEntity receiveMessage(@RequestBody Body body) {
        // Get PubSub message from request body.
        Body.Message message = body.getMessage();
        if (message == null) {
            String msg = "Bad Request: invalid Pub/Sub message format";
            System.out.println(msg);
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        String data = message.getData();
        String target =
                !StringUtils.isEmpty(data) ? new String(Base64.getDecoder().decode(data)) : "World";
        String msg = "Hello " + target + "!";
        System.out.println(msg);
        return new ResponseEntity(msg, HttpStatus.OK);
    }


    private String decode(String data) {
        return new String(Base64.getDecoder().decode(data));
    }

    @RequestMapping("/pull")
    public void greetingS() throws Exception {
        SubscribeMsgService se = new SubscribeMsgService();
        se.receiveMsg();

    }


}
