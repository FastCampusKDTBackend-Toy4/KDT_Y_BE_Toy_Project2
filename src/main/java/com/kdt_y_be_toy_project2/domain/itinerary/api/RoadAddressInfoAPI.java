package com.kdt_y_be_toy_project2.domain.itinerary.api;

import com.kdt_y_be_toy_project2.domain.model.PlaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.Map;

@Slf4j
@Component
public class RoadAddressInfoAPI {

    private final String url = "https://dapi.kakao.com/v2/local/search/keyword";

    @Value("${api.key}")
    private String key;

    public PlaceInfo getPlaceInfoByKeyword(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        String roadAddressName = keyword;

        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            URI targetUrl = UriComponentsBuilder
                    .fromUriString(url)
                    .queryParam("query", keyword)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
            JSONObject jsonObject = new JSONObject(result.getBody());
            JSONArray jsonArray = jsonObject.getJSONArray("documents");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                roadAddressName = object.getString("road_address_name");

                if (roadAddressName.trim().isEmpty()) {
                    continue;
                }

                return PlaceInfo.builder()
                        .name(object.getString("address_name"))
                        .road_address_name(object.getString("road_address_name"))
                        .x(object.getString("x"))
                        .y(object.getString("y"))
                        .build();
            }

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            log.info("OPEN API 호출 에러입니다.");
        }

        return PlaceInfo.builder().name(keyword)
                .road_address_name("")
                .x("")
                .y("").build();
    }
}
