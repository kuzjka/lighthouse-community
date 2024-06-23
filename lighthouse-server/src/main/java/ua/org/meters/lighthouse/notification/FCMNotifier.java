package ua.org.meters.lighthouse.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Sends power notifications to mobile app using Firebase Cloud Messaging.
 */
@Service
public class FCMNotifier implements PowerNotifier {
    private static final Logger logger = LoggerFactory.getLogger(FCMNotifier.class);

    public static final String TOPIC = "power";

    public FCMNotifier(@Value("${lighthouse.fcm.creds-file}") String credentialsPath) throws IOException {
        GoogleCredentials credentials;
        try (InputStream in = new FileInputStream(credentialsPath)) {
            credentials = GoogleCredentials.fromStream(in);
        }
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();

        FirebaseApp.initializeApp(options);
    }

    @Override
    public void notifyPower(boolean powerOn) {
        logger.debug("Notifying with FCM on state change: {}", powerOn);
        Message message = Message.builder()
                .setTopic(TOPIC)
                .setNotification(Notification.builder()
                        .setTitle("Lighthouse")
                        .setBody(powerOn ? "Світло з'явилось!" : "Світло відключено")
                        .build())
                .putData("power", Boolean.toString(powerOn))
                .build();

        String messageId;
        try {
            messageId = FirebaseMessaging.getInstance().send(message);
            logger.debug("Notification successful, message ID: {}", messageId);
        } catch (FirebaseMessagingException e) {
            throw new NotificationException("Cannot notify with FCM", e);
        }
    }
}
