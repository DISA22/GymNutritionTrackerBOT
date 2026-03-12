package integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.NinjasConfig;
import integration.dto.NutritionData;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@Slf4j
public class NinjasHttpClient {
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private static final String API_NUTRITION_URL = NinjasConfig.getNinjasNutritionUrl();
    private static final String API_KEY = NinjasConfig.getNinjasApiKey();

    public NinjasHttpClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

    }

    public List<NutritionData> getNutrition(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_NUTRITION_URL + encodedQuery))
                    .header("X-Api-Key", API_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            if (statusCode < 200 || statusCode >= 300) {
                log.warn("Something went wrong", response.body());
                return null;
            }

            return objectMapper.readValue(response.body(), new TypeReference<List<NutritionData>>() {
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
