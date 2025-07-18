package org.unibl.etf.fitsocial.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry
public class FirebaseConfig {

    // 2. Učitajte fajl iz classpath-a
    @Value("classpath:serviceAccountKey.json")
    private Resource serviceAccount;

    // 3. Registrujte FirebaseApp kao @Bean
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try (InputStream is = serviceAccount.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(is))
                    // po potrebi: .setDatabaseUrl("https://<VAŠ-PROJEKAT>.firebaseio.com")
                    .build();
            // Provjera da li je već inicijalizirano (u testovima ili pri ponovnom load-u)
            return FirebaseApp.getApps().isEmpty()
                    ? FirebaseApp.initializeApp(options)
                    : FirebaseApp.getInstance();
        }
    }

    // (opciono) ako često koristite FirebaseMessaging:
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }

    @Bean("fcmExecutor")
    public Executor fcmExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(20);
        exec.setMaxPoolSize(100);
        exec.setQueueCapacity(500);
        exec.setThreadNamePrefix("FCM-Async-");
        exec.initialize();
        return exec;
    }
}
