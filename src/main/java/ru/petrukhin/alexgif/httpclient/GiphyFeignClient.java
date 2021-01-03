package ru.petrukhin.alexgif.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.gif-service.name}", url = "${feign.gif-service.url}")
public interface GiphyFeignClient {
    @GetMapping(value = "/v1/gifs/random")
    String getGif(@RequestParam("api_key") String appId, @RequestParam String tag);
}
