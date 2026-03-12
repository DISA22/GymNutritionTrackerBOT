package integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.EdamanConfig;
import integration.dto.NutritionData;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EdamanHttpClient {
    private static final String FOOD_URL = EdamanConfig.getFoodBaseUrl();
    private static final String API_KEY = EdamanConfig.getApiKey();
    private static final String APPLICATION_ID = EdamanConfig.getApplicationId();
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public EdamanHttpClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public List<NutritionData> getNutrition(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOOD_URL + "?app_id="+ APPLICATION_ID + "&app_key=" + API_KEY + "&ingr=" + encodedQuery))
                    .header("User-Agent", "GymNutritionTrackerBot - Java - Version 1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            if (statusCode < 200 || statusCode >= 300) {
                log.warn("Something went wrong", response.body());
                Collections.emptyList();
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode hints = root.path("hints");

            if (hints.isEmpty()) return Collections.emptyList();

            JsonNode food = hints.get(0).path("food");
            JsonNode nutrients = food.path("nutrients");

            NutritionData nutritionData = new NutritionData(
                    food.path("label").asText(),
                    nutrients.path("ENERC_KCAL").asDouble(),
                    nutrients.path("PROCNT").asDouble(),
                    nutrients.path("FAT").asDouble(),
                    nutrients.path("CHOCDF").asDouble()
            );

            return List.of(nutritionData);



        } catch (Exception e) {
            log.error("Ошибка при подключении к URL", e);
            log.error("Проблемный URL: " + FOOD_URL + "?app_id=" + APPLICATION_ID + "...");
            return Collections.emptyList();
        }
    }
}
