package org.example.actuator.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MetricHandlerConfiguration {

    @Bean
    public Advisor metricHandlerAdvisor(MetricHandler metricHandlerAdvice) {
        Pointcut pointcut = AnnotationMatchingPointcut.forMethodAnnotation(Metric.class);
        return new DefaultPointcutAdvisor(pointcut, metricHandlerAdvice);
    }

    @Bean
    @Primary
    public MeterRegistry customizeRegistry(MeterRegistry registry, @Value("${spring.application.name}") String applicationName) {
        registry.config().commonTags("application", applicationName);

        return registry;
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
