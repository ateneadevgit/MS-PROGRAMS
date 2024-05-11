package com.fusm.programs.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientConnector {
    public WebClient connectWebClient(String urlService) {
        return WebClient.builder().baseUrl(urlService).build();
    }
}
