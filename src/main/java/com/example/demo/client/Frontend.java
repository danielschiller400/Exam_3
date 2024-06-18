package com.example.demo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Frontend {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    final private String baseUrl1 = "http://localhost:";
    final private String baseUrl2 = "/demo/api";

    public Frontend() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public ResponseEntity<Map<String, String>> getDispatcher(int port) {
        return restTemplate.getForEntity(baseUrl1 + port + baseUrl2, (Class<Map<String, String>>) (Class<?>) Map.class);
    }

    public Map<String, String> isLinkAvailable(ResponseEntity<Map<String, String>> response) {
        Map<String, String> links = response.getBody();
        return links;
    }

    public ResponseEntity<String> getAllPartnerUniversities(int port) {
        String url = getEndpointUrlDispatcher("getAllPartnerUniversities", port);
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> createPartnerUniversity(int port, String partnerUniversityJson) {
        String url = getEndpointUrlDispatcher("createPartnerUniversity", port);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(partnerUniversityJson, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public ResponseEntity<String> getPartnerUniversityById(String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> updatePartnerUniversity(String url, String updatedPartnerUniversityJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(updatedPartnerUniversityJson, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    public ResponseEntity<Void> deletePartnerUniversity(String url) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }

    public ResponseEntity<String> getAllModulesForPartnerUniversity(String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> createModuleInPartnerUniversity(String url, String moduleJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(moduleJson, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public ResponseEntity<String> getModuleOfPartnerUniversity(String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> updateModuleOfPartnerUniversity(String url, String updatedModuleOfPartnerUniversityJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(updatedModuleOfPartnerUniversityJson, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    public ResponseEntity<Void> deleteModuleOfPartnerUniversity(String url) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }


//----------------------------------------------------------------------------------------------------------------------


    public String getEndpointUrlDispatcher(String rel, int port) {
        ResponseEntity<Map<String, String>> response = restTemplate.getForEntity(baseUrl1 + port + baseUrl2, (Class<Map<String, String>>) (Class<?>) Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> links = response.getBody();
            if (links != null && links.containsKey(rel)) {
                return links.get(rel);
            }
        }
        throw new RuntimeException("Endpoint URL not found for rel: " + rel);
    }

    public String responseToJsonString(ResponseEntity<?> response) {
        try {
            // Die Antwort in ein JSON-String konvertieren
            return objectMapper.writeValueAsString(response.getBody());
        } catch (Exception e) {
            // Wenn ein Fehler auftritt, eine RuntimeException mit einer Fehlermeldung werfen
            throw new RuntimeException("Failed to convert response to JSON string", e);
        }
    }

    public boolean jsonStringContainsLink(String jsonString, String rel){
        // Erstellen des Musters mit Wortgrenzen
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(rel) + "\\b");
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            return true; // Wort gefunden
        }

        return false; // Wort nicht gefunden
    }

    public String findLinkForRelInJson(String jsonString, String rel) {
        // Erstellen des Musters für das gesuchte Wort und den href-Link
        final Pattern pattern = Pattern.compile("\\\\\"" + rel + "\\\\\":\\{\\\\\"href\\\\\":\\\\\"(http[s]?://[^\\\\\"]+)\\\\\"\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(jsonString);

        // Überprüfen, ob das Muster gefunden wird
        if (matcher.find()) {
            return matcher.group(1); // Den href-Link aus der ersten Capture-Gruppe zurückgeben
        }

        return null; // Link nicht gefunden
    }

}





