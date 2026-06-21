package in.anubhavspring.journalApp.service;

import in.anubhavspring.journalApp.api.response.WeatherResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherService {

    @Value("${weatherstack.api.key}")
    private String apiKey;

    private static final String API = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;
//    used to config and our api requests seamlessly

    public WeatherResponse getWeather(String city){
//        String finalApi = API.replace("CITY",city).replace("API_KEY",apiKey);
//        better uri component
            String finalApi = UriComponentsBuilder
                    .fromHttpUrl("https://api.weatherstack.com/current")
                    .queryParam("access_key",apiKey)
                    .queryParam("query",city)
                    .build(true)
                    .toUriString();

//        requestEntity is sent when POST,PUT and DELETE we pass in the body with the request
//        which is then received by the API server.
//        we just have to change the HttpMethod to the respective POST,PUT and DELETE type
//        we can pass the HttpHeaders as well as <K,V> pairs

        try{
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET,null, WeatherResponse.class);//pass in our api url, http method type, request entity and finally our POJO class

            return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
