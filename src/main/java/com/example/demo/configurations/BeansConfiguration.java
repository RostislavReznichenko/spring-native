package com.example.demo.configurations;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagationConfig.SingleBaggageField;
import brave.baggage.BaggagePropagationCustomizer;
import brave.baggage.CorrelationScopeConfig.SingleCorrelationField;
import brave.baggage.CorrelationScopeCustomizer;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
@EnableScheduling
public class BeansConfiguration {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    BaggagePropagationCustomizer baggagePropagationCustomizer() {
        return builder -> {
            builder.add(SingleBaggageField.remote(BaggageField.create("X-CORRELATION-ID")));
            builder.add(SingleBaggageField.local(BaggageField.create("X-CORRELATION-ID")));
        };
    }

    @Bean
    CorrelationScopeCustomizer correlationScopeCustomizer() {
        return builder -> builder.add(SingleCorrelationField.create(BaggageField.create("X-CORRELATION-ID")));
    }

    @Bean
    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }

}
