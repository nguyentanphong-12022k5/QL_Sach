package com.example.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeepAliveService {

    private static final Logger logger = LoggerFactory.getLogger(KeepAliveService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.keep-alive.url}")
    private String keepAliveUrl;

    @Value("${app.keep-alive.enabled:false}")
    private boolean isEnabled;

    /**
     * Pings the application URL every 10 minutes to keep it active on free hosting.
     * fixedRate = 600000 ms (10 minutes)
     */
    @Scheduled(fixedRate = 600000)
    public void pingSelf() {
        if (!isEnabled) {
            return;
        }

        try {
            logger.info("Attempting to keep-alive: Ping to {}", keepAliveUrl);
            String response = restTemplate.getForObject(keepAliveUrl, String.class);
            logger.info("Keep-alive successful! Response: {}", response);
        } catch (Exception e) {
            logger.error("Keep-alive ping failed for URL {}: {}", keepAliveUrl, e.getMessage());
        }
    }
}
