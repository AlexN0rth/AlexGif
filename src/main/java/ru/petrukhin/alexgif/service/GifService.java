package ru.petrukhin.alexgif.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.petrukhin.alexgif.httpclient.GiphyFeignClient;
import ru.petrukhin.alexgif.inner.Inner;
import ru.petrukhin.alexgif.outer.Gif;
import ru.petrukhin.alexgif.outer.Rates;

import java.io.FileWriter;
import java.io.IOException;

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

    public Gif handleGif(Rates today, Rates yesterday, Inner inner) throws IOException {
        Double todayValue = today.getRates().get(inner.getData().getSymbols());
        Double yesterdayValue = yesterday.getRates().get(inner.getData().getSymbols());
        if (todayValue >= yesterdayValue) {
            tag = "rich";
        }
        Gif gif = mapper.readValue(giphyFeignClient.getGif(appId, tag), Gif.class);
        createJson(gif);
        return gif;
    }

    private void createJson(Gif gif) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            mapper.writeValue(writer, gif);
        }
    }
}
