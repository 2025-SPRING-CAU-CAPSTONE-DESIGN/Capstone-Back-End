package com.capstone.storyforest.filtering.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter(objectMapper)));
        return restTemplate;
    }
}