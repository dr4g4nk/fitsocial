package core.integrator;

import core.listener.SoftDeleteListener;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

// Ovo su ključni importi za servis listenera:
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;

public class SoftDeleteIntegrator implements Integrator {
    @Override
    public void integrate(Metadata metadata, BootstrapContext bootstrapContext,              // iz org.hibernate.boot.spi
                          SessionFactoryImplementor sessionFactory        // implementor za dobiti pravi registry
    ) {
        // ovo je ključna razlika:
        EventListenerRegistry registry = sessionFactory.getServiceRegistry()                        // ovdje uzimamo SessionFactoryServiceRegistry
                .getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.DELETE).appendListeners(new SoftDeleteListener());
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        // nema posebne logike
    }
}