package org.ecoflow.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.ecoflow.common.inputs.AddressInput;
import org.ecoflow.common.inputs.MapMarkerInput;
import org.ecoflow.common.models.Coordinates;
import org.ecoflow.common.models.markers.Marker;
import org.ecoflow.common.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Path("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @POST
    @Path("/markers")
    public Response retrieveMarkersForCoords(MapMarkerInput input) {

        if (input == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{ 'erreur' : 'No input found'}").build();
        }

        List<Marker> markers = mapService.fetchMarkersFromCoord(input.getLat(), input.getLon(), input.getAmenities(), input.getRecyclingFilters());

        if (markers.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).entity("No markers found").build();
        }

        return Response.accepted().entity(markers).build();
    }

    @POST
    @Path("/reverse")
    public Response findCoordinatesByAddress(AddressInput input) {

        if (input.getAddress().isEmpty() || input.getCity().isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();

        try {
            Coordinates coordinates = mapService.fetchCoordinatesFromAddress(input);

            if (coordinates == null) return Response.status(Response.Status.FORBIDDEN).build();

            return Response.status(Response.Status.ACCEPTED).entity(coordinates).build();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

}
