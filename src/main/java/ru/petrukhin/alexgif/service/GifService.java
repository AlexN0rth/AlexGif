package ru.petrukhin.alexgif.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.petrukhin.alexgif.httpclient.GiphyFeignClient;
import ru.petrukhin.alexgif.outer.Gif;
import ru.petrukhin.alexgif.outer.Rate;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Service
public class GifService {
    private static String tag = "broke";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    GiphyFeignClient giphyFeignClient;
    @Value("${feign.gif-service.app-id}")
    private String appId;
    @Value("${json.outer.path}")
    private String path;

    public Gif handleGif(Rate todayRate, Rate yesterdayRate, String symbols) {
        Gif gif = null;
        if (todayRate == null || yesterdayRate == null) {
            return gif;
        }
        Double todayValue = todayRate.getRates().get(symbols);
        Double yesterdayValue = yesterdayRate.getRates().get(symbols);
        if (todayValue >= yesterdayValue) {
            tag = "rich";
        }
        try {
            gif = mapper.readValue(giphyFeignClient.getGif(appId, tag), Gif.class);
        } catch (FeignException e) {
            log.info("Gif service is not responding");
            return gif;
        } catch (JsonProcessingException e) {
            log.info("Read gif response exception");
        }
        try {
            createJson(gif);
        } catch (IOException e) {
            log.info("JSON file write exception");
        }
        return gif;
    }

    private void createJson(Gif gif) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            mapper.writeValue(writer, gif);
        }
    }
}
