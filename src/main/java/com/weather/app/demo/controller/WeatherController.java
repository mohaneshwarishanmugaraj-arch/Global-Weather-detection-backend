package com.weather.app.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "https://global-weather-detection-frontend.vercel.app/")
public class WeatherController {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @GetMapping
    public Map<String, Object> getWeather(@RequestParam String city) {

        String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        Map<String, Object> main = (Map<String, Object>) response.get("main");
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        Map<String, Object> sys = (Map<String, Object>) response.get("sys");
        Map<String, Object> coord = (Map<String, Object>) response.get("coord");

        List<Map<String, Object>> weatherList =
                (List<Map<String, Object>>) response.get("weather");

        Map<String, Object> weather = weatherList.get(0);

        Map<String, Object> result = new HashMap<>();

        // 📍 Location
        result.put("city", response.get("name"));
        result.put("country", sys.get("country"));
        result.put("latitude", coord.get("lat"));
        result.put("longitude", coord.get("lon"));

        // 🌡 Temperature
        result.put("temperature", main.get("temp"));
        result.put("feelsLike", main.get("feels_like"));
        result.put("tempMin", main.get("temp_min"));
        result.put("tempMax", main.get("temp_max"));
        result.put("humidity", main.get("humidity"));
        result.put("pressure", main.get("pressure"));

        // 🌥 Weather
        result.put("weatherMain", weather.get("main"));
        result.put("weatherDescription", weather.get("description"));
        result.put("icon", weather.get("icon"));

        // 🌬 Wind
        result.put("windSpeed", wind.get("speed"));
        result.put("windDegree", wind.get("deg"));
        result.put("windGust", wind.get("gust"));

        // 👁 Visibility
        result.put("visibility", response.get("visibility"));

        // 🌅 Sun
        result.put("sunrise", sys.get("sunrise"));
        result.put("sunset", sys.get("sunset"));

        return result;
    }
}
