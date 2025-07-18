package org.unibl.etf.fitsocial;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.ResourceUtils;
import org.unibl.etf.fitsocial.configuration.FileStorageProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(FileStorageProperties.class)
public class FitsocialApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(FitsocialApplication.class, args);
	}

}
