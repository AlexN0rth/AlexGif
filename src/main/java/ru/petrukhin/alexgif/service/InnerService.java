package ru.petrukhin.alexgif.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.petrukhin.alexgif.inner.Inner;

import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
public class InnerService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${json.inner.path}")
    private String path;

    public String handleInnerJson() {
        Inner inner;
        try (FileReader reader = new FileReader(path)) {
            inner = mapper.readValue(reader, Inner.class);

        } catch (IOException e) {
            log.info("JSON file read exception");
            return "";
        }
        return inner.getData().getSymbols();
    }

}
