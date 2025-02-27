package org.ecoflow.common.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.ecoflow.common.inputs.EventInput;
import org.ecoflow.common.models.Event;
import org.ecoflow.common.repositories.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.listAll();
    }

    public Optional<Event> getEventById(long id) {
        return eventRepository.findByIdOptional(id);
    }

    public List<Event> getEventsByCity(String city) {
        return eventRepository.findByCity(city);
    }

    @Transactional
    public Event createEvent(EventInput event) {
        Event eventCreated = new Event(event.getName(), event.getDescription(), event.getAddress(), event.getCity(), event.getNbUsers());
        eventCreated.setId(UUID.randomUUID());
        eventRepository.persist(eventCreated);
        return eventCreated;
    }

    @Transactional
    public void deleteEvent(long id) {
        eventRepository.deleteById(id);
    }
}
