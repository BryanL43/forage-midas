package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveAPI {
    private final RestTemplate restTemplate;
    private final String incentiveAPIUrl;

    public IncentiveAPI(RestTemplateBuilder builder, @Value("${general.incentive-api-url}") String incentiveAPIUrl) {
        this.restTemplate = builder.build();
        this.incentiveAPIUrl = incentiveAPIUrl;
    }

    public Incentive query(Transaction transaction) {
        return restTemplate.postForObject(incentiveAPIUrl, transaction, Incentive.class);
    }
}
