package org.ecoflow.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecoflow.common.inputs.EventInput;
import org.ecoflow.common.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GET()
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents() {
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(eventService.getAllEvents())
                .build();
    }

    @POST()
    @Path("/create")
    public Response addEvent(EventInput event) {
        eventService.createEvent(event);
        return Response.status(Response.Status.CREATED)
                .entity(event)
                .build();
    }
}
