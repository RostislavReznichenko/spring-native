package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static org.springframework.util.StringUtils.hasLength;

@Slf4j
public class SystemTrustStoreInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private static final String TRUSTSTORE_PATH_KEY = "truststore.path";
    private static final String TRUSTSTORE_PASSWORD_KEY = "truststore.password";
    private static final String JAVAX_TRUSTSTORE_PATH = "javax.nex.ssl.trustStore";
    private static final String JAVAX_TRUSTSTORE_PASSWORD = "javax.nex.ssl.trustStorePassword";

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("System TrustStore initializing..");
        var environment = applicationContext.getEnvironment();
        var trustStorePath = environment.getProperty(TRUSTSTORE_PATH_KEY);
        var trustStorePassword = environment.getProperty(TRUSTSTORE_PASSWORD_KEY);
        if (!hasLength(trustStorePath) || !hasLength(trustStorePassword)) {
            return;
        }
        var trustStoreAbsPath = getTrustStoreAbsPath(trustStorePath);
        if (!hasLength(trustStoreAbsPath)) {
            return;
        }
        System.setProperty(JAVAX_TRUSTSTORE_PATH, trustStoreAbsPath);
        System.setProperty(JAVAX_TRUSTSTORE_PASSWORD, trustStorePassword);
        log.info("System TrustStore initialized [{}]", trustStoreAbsPath);
    }

    private String getTrustStoreAbsPath(String trustStorePath) {
        var resource = resourceLoader.getResource(trustStorePath);
        if (resource.exists()) {
            log.warn("TrustStore resource not found by path [{}]", trustStorePath);
            return null;
        }
        try {
            return resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
