package org.unibl.etf.fitsocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FitsocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitsocialApplication.class, args);
	}

}
