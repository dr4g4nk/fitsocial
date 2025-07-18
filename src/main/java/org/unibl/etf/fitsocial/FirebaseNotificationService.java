package org.unibl.etf.fitsocial;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FirebaseNotificationService {

    private static final Logger log = LoggerFactory.getLogger(FirebaseNotificationService.class);
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    /**
     * Asinkrono šalje FCM poruku i retry-a do 3 puta na FirebaseMessagingException.
     */
    @Async("fcmExecutor")
    @Retryable(
            value = FirebaseMessagingException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void sendNotificationAsync(String targetToken, String title, String body, String imageUrl, Map<String, String> data) throws FirebaseMessagingException {

        var notificationBuilder = Notification.builder()
                .setTitle(title)
                .setBody(body);
        if (imageUrl != null && !imageUrl.isBlank())
            notificationBuilder.setImage(imageUrl);

        var builder = Message.builder()
                .setToken(targetToken)
                .setNotification(notificationBuilder.build());

        if (data != null && !data.isEmpty()) {
            builder = builder.putAllData(data);
        }

        var msg = builder.build();
        String response = firebaseMessaging.send(msg);
        log.info("FCM poruka uspješno poslata, id={}", response);
    }

    /**
     * Ova metoda se poziva ako su svi retry pokušaji zakašnjeli.
     */
    @Recover
    public void recover(FirebaseMessagingException ex, String targetToken, String title, String body, Map<String, String> data) {
        log.error("FCM: svi retry pokušaji za token={} zakašnjeli: {}", targetToken, ex.getMessage());
        // ovdje npr. obavještavanje admina ili zapis u bazu
    }
}