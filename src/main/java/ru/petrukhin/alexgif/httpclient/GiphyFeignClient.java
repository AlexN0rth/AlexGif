package ru.petrukhin.alexgif.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This interface represents the Feign Client, which sends a request to the API of the external service "Giphy"
 *
 * @author Alex
 * @see ru.petrukhin.alexgif.service.GifService
 */
@FeignClient(name = "${feign.gif-service.name}", url = "${feign.gif-service.url}")
public interface GiphyFeignClient {
    @GetMapping(value = "/v1/gifs/random")
    String getGif(@RequestParam("api_key") String appId, @RequestParam String tag);
}
