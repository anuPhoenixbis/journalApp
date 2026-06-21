package in.anubhavspring.journalApp.service;

import in.anubhavspring.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    private static final String apiKey = "786e38740187c65fdfbe6c4d11e1fa9d";

    private static final String API = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;
//    used to config and our api requests seamlessly

    public WeatherResponse getWeather(String city){
        String finalApi = API.replace("CITY",city).replace("API_KEY",apiKey);

//        requestEntity is sent when POST,PUT and DELETE we pass in the body with the request
//        which is then received by the API server.
//        we just have to change the HttpMethod to the respective POST,PUT and DELETE type
//        we can pass the HttpHeaders as well as <K,V> pairs

        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET,null, WeatherResponse.class);//pass in our api url, http method type, request entity and finally our POJO class

        return response.getBody();
    }
}
