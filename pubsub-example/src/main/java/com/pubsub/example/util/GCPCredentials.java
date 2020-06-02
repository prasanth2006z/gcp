package com.pubsub.example.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.pubsub.example.constants.Constants;

import java.io.FileInputStream;

public class GCPCredentials {

    public static GoogleCredentials getCredentials() throws Exception{
        return GoogleCredentials.fromStream(new FileInputStream(Constants.SERVICE_ACCOUNT_FILE));
    }
}
