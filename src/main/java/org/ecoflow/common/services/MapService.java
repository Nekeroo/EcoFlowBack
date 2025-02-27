package org.ecoflow.common.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.ecoflow.common.inputs.AddressInput;
import org.ecoflow.common.models.Coordinates;
import org.ecoflow.common.models.markers.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MapService {

    @Autowired
    private MarkerService markerService;

    public List<Marker> fetchMarkersFromCoord(float lat, float lon, List<String> amenities, List<String> recyclingFilters) {

        if (amenities.isEmpty()) return new ArrayList<>();

        try {
            String overpassQuery = this.castUriFromFitlers(amenities, recyclingFilters, lat, lon);

            String encodedQuery = URLEncoder.encode(overpassQuery, StandardCharsets.UTF_8);
            String uri = "https://overpass-api.de/api/interpreter?data=" + encodedQuery;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsing du JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode elements = root.path("elements");

            List<Marker> nodes = new ArrayList<>();
            if (elements.isArray()) {
                for (JsonNode node : elements) {
                    // Récupération des tags
                        Marker marker = markerService.mapDataAsMarker(node, mapper);

                        nodes.add(marker);
                }
            }
            return nodes;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public String castUriFromFitlers(List<String> amenities, List<String> recyclingFilters, double lat, double lon) {
        List<String> amenitiesQueries = amenities.stream().map(
                        amenity -> String.format("node[\"amenity\"=\"%s\"](around:%s,%s,%s);", amenity, 1000, lat, lon))
                .toList();

        List<String> recyclingQueries =
                !recyclingFilters.isEmpty() ? recyclingFilters.stream().map(recyclingType -> String.format("node[\"recycling:%s\"=\"yes\"](around:%s,%s,%s);", recyclingType, 1000, lat, lon))
                        .toList() : new ArrayList<>();

        List<String> combinedQueries = Stream.concat(amenitiesQueries.stream(), recyclingQueries.stream())
                .collect(Collectors.toList());

        // Joindre les requêtes avec un saut de ligne
        String joinedQueries = String.join("\n", combinedQueries);

        // Composer la requête finale
        String overpassQuery = String.format(
                "[out:json];\n(\n%s\n);\nout body;",
                joinedQueries
        );

        return overpassQuery;
    }

    public Coordinates fetchCoordinatesFromAddress(AddressInput input) {
        try {
            String encodedAddress = URLEncoder.encode(input.getAddress(), StandardCharsets.UTF_8);
            String encodedCity = URLEncoder.encode(input.getCity(), StandardCharsets.UTF_8);

            String url = String.format(
                    "https://nominatim.openstreetmap.org/search.php?q=%s,%s&format=jsonv2",
                    encodedAddress, encodedCity
            );

            System.out.println(url);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Vérification du code de réponse HTTP
            if (response.statusCode() != 200) {
                throw new RuntimeException("Erreur HTTP: " + response.statusCode());
            }

            // Parsing du JSON retourné par l'API
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            // Vérification que le tableau de résultats n'est pas vide
            if (!root.isArray() || root.size() == 0) {
                return null;
            }

            // Récupération des coordonnées à partir du premier résultat
            JsonNode latNode = root.get(0).get("lat");
            JsonNode lonNode = root.get(0).get("lon");

            System.out.println(lonNode + " - "  + lonNode);

            if (latNode.asText().isEmpty() || lonNode.asText().isEmpty()) {
                return null;
            }
            double lat = root.get(0).get("lat").asDouble();
            double lon = root.get(0).get("lon").asDouble();

            return new Coordinates(lat, lon);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
