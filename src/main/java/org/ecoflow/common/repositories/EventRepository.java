package org.ecoflow.common.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.ecoflow.common.models.Event;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {

    // Rechercher un événement par son nom
    public List<Event> findByCity(String city) {
        return list("city", city);
    }
}
