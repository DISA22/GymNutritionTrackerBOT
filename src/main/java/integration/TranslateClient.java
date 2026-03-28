package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TranslateConfig;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TranslateClient {
    private static final String URL = TranslateConfig.getUrl();
    private static final String FROM_EN_TO_RU = TranslateConfig.getFromEnToRu();
    private static final String FROM_RU_TO_EN = TranslateConfig.getFromRuToEn();

    private final Map<String, String> translateMap = new ConcurrentHashMap<String, String>();

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TranslateClient(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = objectMapper;
    }

    public String translateFromEnToRu(String str) {
        try {
            String encodedStr = URLEncoder.encode(str, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + FROM_EN_TO_RU + encodedStr))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                log.warn("Something went wrong", response.body());
                return str;
            }

            JsonNode node = objectMapper.readTree(response.body());

            return node.asText();
        } catch (Exception e) {
            log.error("Translate error: {}", e.getMessage());
            return str;
        }
    }

    public String translateFromRuToEn(String str) {
        try {
            String encodedStr = URLEncoder.encode(str, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + FROM_RU_TO_EN + encodedStr))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                log.warn("Something went wrong", response.body());
                return str;
            }

            JsonNode node = objectMapper.readTree(response.body());

            return node.asText();
        } catch (Exception e) {
            log.error("Translate error: {}", e.getMessage());
            return str;
        }
    }

    public String cacheTranslateToRu(String str) {
        if (str == null || str.isBlank()) return str;

        return translateMap.computeIfAbsent(str, this::translateFromEnToRu);
    }


}
