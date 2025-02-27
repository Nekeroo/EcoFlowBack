package org.ecoflow.common.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ecoflow.common.models.AmenityLabel;
import org.ecoflow.common.models.markers.Marker;
import org.ecoflow.common.models.markers.Tags;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MarkerService {

    MarkerService() { }

    public Marker mapDataAsMarker(JsonNode node, ObjectMapper mapper) {
        // Récupération des tags
        JsonNode tagsNode = node.path("tags");
        Map<String, String> tags = mapper.convertValue(tagsNode, Map.class);

        // Récupération du type d'amenity ou chaîne vide par défaut
        String amenityType = tags.getOrDefault("amenity", "");

        // Filtrage optionnel des types de recyclage (similaire à recyclingData en TS)
        List<String> recyclingData = tags.entrySet().stream()
                .filter(e -> e.getKey().startsWith("recycling:") && "yes".equals(e.getValue()))
                .map(e -> e.getKey().replace("recycling:", ""))
                .toList();
        // Vous pouvez utiliser recyclingData si besoin

        // Détermination du nom à afficher
        String name = tags.get("name");
        String amenityLabel = AmenityLabel.getLabelByCode(amenityType);
        if (name == null || name.isEmpty()) {
            name = (amenityLabel != null ? amenityLabel : "Lieu de recyclage");
        }

        // Création d'un objet OSMNode
        Marker osmNode = new Marker();
        osmNode.setId(node.path("id").asLong());
        osmNode.setLat(node.path("lat").asDouble());
        osmNode.setLon(node.path("lon").asDouble());
        osmNode.setName(name);

        // Ajout des tags (ici, on crée une map simplifiée)
        Tags tag = new Tags(amenityType, tags.get("name") != null ? tags.get("name") : amenityLabel, tags.get("recycling_type"));
        osmNode.setTags(tag);

        return new Marker(node.path("id").asInt(), "", tag, name, node.path("lon").asDouble(), node.path("lat").asDouble());
    }


}
