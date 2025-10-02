package org.unibl.etf.fitsocial.notification;

import com.google.firebase.messaging.*;
import java.util.HashMap;
import java.util.Map;

public final class FcmConfigs {

    private FcmConfigs() {}

    /**
     * APNs config za ALERT notifikacije (prikaz + opcioni rich media).
     *
     * @param category       iOS UNNotificationCategory id (npr. "CHAT_REPLY")
     * @param threadId       iOS thread id (grupisanje u Notification Centeru)
     * @param sound          naziv zvuka (npr. "default")
     * @param badge          broj za bedž (null ako ne šalješ)
     * @param imageUrlForNSE URL slike koju će Notification Service Extension preuzeti (pošalji kao custom ključ, npr. "media-url")
     * @param extraApsData   dodatni custom Aps podaci (npr. {"media-type":"image"})
     * @return ApnsConfig
     */
    public static ApnsConfig buildApnsAlertConfig(
            String title,
            String body,
            String category,
            String threadId,
            Boolean contentAvailable,
            String sound,
            Integer badge,
            String imageUrlForNSE,
            Map<String, Object> extraApsData
    ) {
        Aps.Builder aps = Aps.builder()
                .setSound(sound == null ? "default" : sound)
                .setAlert(ApsAlert.builder().setTitle(title).setBody(body).build())
                .setMutableContent(true)
                .setContentAvailable(contentAvailable)
                .setCategory(category)
                .setThreadId(threadId);

        if (badge != null) aps.setBadge(badge);

        if (extraApsData != null) {
            extraApsData.forEach(aps::putCustomData);
        }
        if (imageUrlForNSE != null && !imageUrlForNSE.isBlank()) {
            aps.putCustomData("media-url", imageUrlForNSE);
        }

        // Preporučeni header-i za alert push
        Map<String, String> headers = new HashMap<>();
        headers.put("apns-push-type", "alert");
        headers.put("apns-priority", "10"); // 10 = odmah, 5 = opportunistic

        return ApnsConfig.builder()
                .setAps(aps.build())
                .putAllHeaders(headers)
                .build();
    }

    /**
     * APNs config za BACKGROUND (data-only) notifikacije.
     * iOS će probuditi app (tihim putem) ako sistem dozvoli.
     */
    public static ApnsConfig buildApnsBackgroundConfig() {
        Aps aps = Aps.builder()
                .setContentAvailable(true)  // background update
                .setMutableContent(false)
                .build();

        Map<String, String> headers = new HashMap<>();
        headers.put("apns-push-type", "background");
        headers.put("apns-priority", "5"); // background mora biti 5

        return ApnsConfig.builder()
                .setAps(aps)
                .putAllHeaders(headers)
                .build();
    }

    /**
     * Android config za ALERT notifikacije (visok prioritet).
     *
     * @param channelId   NotificationChannel id (mora postojati u app-u)
     * @param collapseKey ključ za spajanje višestrukih poruka (npr. "chat-<id>")
     * @param ttlSeconds  time-to-live u sekundama
     * @param clickAction aktivnost/intent action (npr. "OPEN_CHAT")
     * @param imageUrl    URL slike (ako želiš prikaz slike na Androidu 8+)
     */
    public static AndroidConfig buildAndroidAlertConfig(
            String title,
            String body,
            String channelId,
            String collapseKey,
            long ttlSeconds,
            String clickAction,
            AndroidNotification.Priority priority,
            String imageUrl
    ) {
        AndroidNotification.Builder notif = AndroidNotification.builder()
                .setChannelId(channelId)
                .setTitle(title)
                .setBody(body)
                .setClickAction(clickAction)
                .setPriority(priority)
                .setDefaultVibrateTimings(true)
                .setDefaultSound(true);

        if (imageUrl != null && !imageUrl.isBlank()) {
            notif.setImage(imageUrl);
        }

        AndroidConfig.Builder b = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setTtl(ttlSeconds)
                .setNotification(notif.build());

        if (collapseKey != null && !collapseKey.isBlank()) {
            b.setCollapseKey(collapseKey);
        }

        return b.build();
    }

    /**
     * Android config za DATA-ONLY (silent) isporuku sa visokim prioritetom.
     */
    public static AndroidConfig buildAndroidDataOnlyConfig(long ttlSeconds, String collapseKey) {
        AndroidConfig.Builder b = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setTtl(ttlSeconds);

        if (collapseKey != null && !collapseKey.isBlank()) {
            b.setCollapseKey(collapseKey);
        }
        return b.build();
    }


}