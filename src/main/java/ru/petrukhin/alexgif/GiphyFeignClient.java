package ru.petrukhin.alexgif;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${feign.gif-service.name}", url = "${feign.gif-service.url}")
public interface GiphyFeignClient {

}
