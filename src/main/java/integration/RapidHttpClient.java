package integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.RapidConfig;
import integration.dto.ExerciseDto;
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
public class RapidHttpClient {
    private static final String URL = RapidConfig.getUrl();
    private static final String API_KEY = RapidConfig.getApiKey();

    private final TranslateClient translateClient;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public RapidHttpClient(TranslateClient translateClient, ObjectMapper objectMapper) {
        this.translateClient = translateClient;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public List<ExerciseDto> getListExercise(String muscleGroupRu) {
        try {
            String muscleGroupEn = translateClient.translateFromRuToEn(muscleGroupRu);
            String encodedQuery = URLEncoder.encode(muscleGroupEn, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + encodedQuery))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                    .header("Content-Type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode < 200 || statusCode > 300) {
                log.error("RapidApi Error: Status: {} . Body: {}", statusCode, response.body());
                return List.of();
            }

            return objectMapper.readValue(response.body(), new TypeReference<List<ExerciseDto>>() {
            });

        } catch (Exception e) {
            log.error("Network or parcing error:{} ", e.getMessage());
            return List.of();
        }
    }
}
