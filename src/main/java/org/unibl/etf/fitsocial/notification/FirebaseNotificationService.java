package org.unibl.etf.fitsocial.notification;

import com.google.firebase.messaging.*;
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
    public void sendNotificationAsync(String targetToken, Map<String, String> notifikationData, ApnsConfig apnsConfig, AndroidConfig androidConfig) throws FirebaseMessagingException {

        var builder = Message.builder()
                .setToken(targetToken);


        if(apnsConfig != null)
            builder.setApnsConfig(apnsConfig);
        if(androidConfig != null)
            builder.setAndroidConfig(androidConfig);

        if (notifikationData != null && !notifikationData.isEmpty()) {
            builder = builder.putAllData(notifikationData);
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