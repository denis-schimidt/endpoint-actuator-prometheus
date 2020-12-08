package org.example.actuator.metrics;

import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MetricFilterAutoConfiguration.class)
public class MetricHandlerConfiguration {

    @Bean
    public Advisor metricHandlerAdvisor(MetricHandler metricHandlerAdvice) {
        Pointcut pointcut = AnnotationMatchingPointcut.forMethodAnnotation(Metric.class);
        return new DefaultPointcutAdvisor(pointcut, metricHandlerAdvice);
    }

    @Bean
    public MetricHandler metricHandlerAdvice(MetricSender metricSender) {
        return new MetricHandler(metricSender);
    }

    @Bean
    public MetricSender metricSender(CounterService counterService, GaugeService gaugeService) {
        return new MetricSender(counterService, gaugeService);
    }
}
