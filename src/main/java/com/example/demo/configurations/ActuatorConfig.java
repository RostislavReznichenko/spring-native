package com.example.demo.configurations;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import io.micrometer.observation.ObservationPredicate;
import net.ttddyy.observation.tracing.DataSourceBaseContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.ServerHttpObservationFilter;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Configuration(proxyBeanMethods = false)
public class ActuatorConfig {

    @Bean
    ObservationPredicate noActuatorServerObservations() {
        return (name, context) -> {
            if (name.equals("http.server.requests") && context instanceof ServerRequestObservationContext serverContext) {
                return !serverContext.getCarrier().getRequestURI().startsWith("/actuator");
            }
            else {
                return true;
            }
        };
    }

    @Bean
    ObservationPredicate noRootlessHttpObservations() {
        return (name, context) -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                Observation observation = (Observation) requestAttributes.getAttribute(ServerHttpObservationFilter.class.getName() + ".observation", SCOPE_REQUEST);
                return observation == null || !observation.isNoop();
            }
            else {
                return true;
            }
        };
    }

    @Bean
    ObservationFilter tempoErrorFilter() {
        // TODO: remove this once Tempo is fixed: https://github.com/grafana/tempo/issues/1916
        return context -> {
            if (context.getError() != null) {
                context.addHighCardinalityKeyValue(KeyValue.of("error", "true"));
                context.addHighCardinalityKeyValue(KeyValue.of("errorMessage", context.getError().getMessage()));
            }
            return context;
        };
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(DataSourceBaseContext.class)
    static class DataSourceActuatorConfig {
        @Bean
        ObservationFilter tempoServiceGraphFilter() {
            // TODO: remove this once Tempo is fixed: https://github.com/grafana/tempo/issues/2212
            return context -> {
                if (context instanceof DataSourceBaseContext dataSourceContext) {
                    context.addHighCardinalityKeyValue(KeyValue.of("db.name", dataSourceContext.getRemoteServiceName()));
                }
                return context;
            };
        }
    }

}
