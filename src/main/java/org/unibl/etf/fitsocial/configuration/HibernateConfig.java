package org.unibl.etf.fitsocial.configuration;

import core.integrator.SoftDeleteIntegrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HibernateConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProps -> {
            hibernateProps.put(
                    // Hibernate setting za IntegratorProvider
                    "hibernate.integrator_provider",
                    (IntegratorProvider) () -> List.of(new SoftDeleteIntegrator())
            );
        };
    }
}