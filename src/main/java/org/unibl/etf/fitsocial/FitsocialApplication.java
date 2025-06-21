package org.unibl.etf.fitsocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.unibl.etf.fitsocial.configuration.FileStorageProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(FileStorageProperties.class)
public class FitsocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitsocialApplication.class, args);
	}

}
