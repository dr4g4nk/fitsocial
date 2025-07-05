package core.listener;

import core.entity.SoftDeletableEntity;
import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultDeleteEventListener;
import org.hibernate.event.spi.DeleteContext;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;

import java.time.Instant;

public class SoftDeleteListener  implements DeleteEventListener {
    private final DefaultDeleteEventListener delegate = new DefaultDeleteEventListener();

    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {
        onDelete(event, DeleteContext.create());
    }

    @Override
    public void onDelete(DeleteEvent event, DeleteContext transientEntities) throws HibernateException {
        Object entity = event.getObject();
        if (entity instanceof SoftDeletableEntity e) {
            e.setDeletedAt(Instant.now());
            event.getSession().merge(e);
        } else {
            delegate.onDelete(event, transientEntities);
        }
    }
}

