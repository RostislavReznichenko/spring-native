package com.example.demo;

import io.micrometer.observation.aop.ObservedAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import static org.springframework.util.ReflectionUtils.findMethod;

public class ObservedAspectRuntimeRegistrar implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        var method = findMethod(ObservedAspect.class, "observeMethod", ProceedingJoinPoint.class);
        hints.reflection().registerMethod(method, ExecutableMode.INVOKE);
        hints.resources().registerPattern("vault/application-secret.yml");
    }
}
