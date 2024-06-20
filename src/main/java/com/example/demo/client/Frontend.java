package com.example.demo.client;

import com.example.demo.api.controller.RelTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

    public ResponseEntity<String> getAllPartnerUniversities(String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> createPartnerUniversity(String url, String partnerUniversityJson) {
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

    public String responseToJsonString(ResponseEntity<?> response) {
        try {
            // Die Antwort in ein JSON-String konvertieren
            return objectMapper.writeValueAsString(response.getBody());
        } catch (Exception e) {
            // Wenn ein Fehler auftritt, eine RuntimeException mit einer Fehlermeldung werfen
            throw new RuntimeException("Failed to convert response to JSON string", e);
        }
    }

    public String findLinkForRelInJson(String jsonString, String rel) {
        // Erstellen des Musters für das gesuchte Wort und den href-Link
        final Pattern pattern = Pattern.compile("\\\\\"" + rel + "\\\\\":\\{\\\\\"href\\\\\":\\\\\"(http[s]?://[^\\\\\"]+)\\\\\"\\,", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(jsonString);

        // Überprüfen, ob das Muster gefunden wird
        if (matcher.find()) {
            return matcher.group(1); // Den href-Link aus der ersten Capture-Gruppe zurückgeben
        }

        return null; // Link nicht gefunden
    }




    public boolean searchForRelInResponse(List<String> list, String rel) {

        boolean found = false;
        for (String s : list) {
            if (containsRel(s, rel)){
                found = true;
                break;
            }
        }
        return found;
    }

    private boolean containsRel(String linkHeader, String relValue) {
        String regex = "rel=\"" + relValue + "\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(linkHeader);

        while (matcher.find()) {
            return true;

        }
        return false;
    }

    public String getLinkFromRel(List<String> list, String rel) {
        for (String s : list) {
            if (containsRel(s, rel)){
                System.out.println("Test: " + s);
                String url = extractLink(s, rel);
                return url;
            }
        }
        return null;
    }


    private String extractLink(String linkHeader, String relValue) {
        String linkRegex = "<([^>]*)>";
        String relRegex = "rel=\"" + relValue + "\"";

        Pattern linkPattern = Pattern.compile(linkRegex);
        Pattern relPattern = Pattern.compile(relRegex);

        Matcher linkMatcher = linkPattern.matcher(linkHeader);
        Matcher relMatcher = relPattern.matcher(linkHeader);

        while (linkMatcher.find() && relMatcher.find()) {
            String link = linkMatcher.group(1);
            return link;
        }
        return null;
    }

}





