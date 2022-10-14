package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * getSearchResults(query): Observable<HttpResponse<any>> {
 *     return this.http.get<IAccount>(`${this.url}&input=${query}`, { observe: 'response' });
 *   }
 *
 *   getPlaceByPlaceId(placeId): Observable<HttpResponse<any>> {
 *       return this.http.get<IAccount>(
 *         `https://maps.googleapis.com/maps/api/place/details/json?place_id=${placeId}&key=${GOOGLE_PLACES_API_KEY}`,
 *         { observe: 'response' });
 *   }
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/places-api")
public class GooglePlacesAPIProxyController {

    private static final String GOOGLE_PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyCWfP_3D3jCTlXdMvHQa8Y_r0r_iov83Yc&radius=3200000&strictbounds=true&location=-25.274398%2C133.775136&components=country:au";
    private final RestTemplate restTemplate;

    @GetMapping("/query")
    public String queryPlaces(@RequestParam("query") String query) {
        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json; charset=UTF-8");
//        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");
        String url  =  GOOGLE_PLACES_API_URL + "&input=" + query;
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

        WebClient client = WebClient.create();

        WebClient.ResponseSpec responseSpec = client.get()
                .uri("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyCWfP_3D3jCTlXdMvHQa8Y_r0r_iov83Yc&radius=3200000&strictbounds=true&location=-25.274398%2C133.775136&components=country:au&input=123")
                .retrieve();

        String responseBody = responseSpec.bodyToMono(String.class).block();
        return responseBody;
    }
}
