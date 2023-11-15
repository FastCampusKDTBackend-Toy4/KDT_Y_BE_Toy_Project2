package com.kdt_y_be_toy_project2.domain.itinerary.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.kdt_y_be_toy_project2.domain.itinerary.exception.ApiConnectionException;
import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class RoadAddressInfoAPI {

    private final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword";

    @Value("${kakao.rest.api-key}")
    private String key;

    public PlaceInfo getPlaceInfoByKeyword(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);

        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            URI targetUrl = UriComponentsBuilder
                    .fromUriString(KAKAO_API_URL)
                    .queryParam("query", keyword)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, JsonNode.class);
            JsonNode documentsNode = Objects.requireNonNull(responseEntity.getBody()).get("documents");

            for (JsonNode documentNode : documentsNode) {
                String roadAddressName = documentNode.get("road_address_name").asText();

                if (roadAddressName.trim().isEmpty()) {
                    continue;
                }

                return PlaceInfo.builder()
                        .name(documentNode.get("address_name").asText())
                        .roadAddressName(roadAddressName)
                        .x(documentNode.get("x").asText())
                        .y(documentNode.get("y").asText())
                        .build();
            }


        } catch (HttpClientErrorException e) {
            throw new ApiConnectionException();
        }

        return PlaceInfo.builder().name(keyword)
                .roadAddressName("")
                .x("")
                .y("").build();
    }
}
