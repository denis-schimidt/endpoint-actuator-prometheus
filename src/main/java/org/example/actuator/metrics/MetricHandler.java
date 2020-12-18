package org.example.actuator.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.example.actuator.metrics.MetricTypeSender.*;

@Component
public class MetricHandler implements MethodInterceptor {
    private final MeterRegistry meterRegistry;

    @Autowired
    MetricHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Metric metric = getMetric(methodInvocation);
        String prefix = metric.prefix();
        Class<? extends Throwable>[] warningExceptions = metric.warningFor();

        try {
            ATTEMPTED.increment(prefix, meterRegistry);
            Object result = methodInvocation.proceed();
            SUCCEEDED.increment(prefix, meterRegistry);

            return result;

        } catch (Exception e) {
            for (Class<? extends Throwable> warningException : warningExceptions) {
                if (warningException.isAssignableFrom(e.getClass())) {
                    WARNING.increment(prefix, meterRegistry);
                    throw e;
                }
            }

            FAILED.increment(prefix, meterRegistry);
            throw e;
        }
    }

    private Metric getMetric(MethodInvocation methodInvocation) throws NoSuchMethodException {
        Class<? extends Object> baseClass = methodInvocation.getThis().getClass();
        Method methodFromBaseClass = baseClass.getMethod(
                methodInvocation.getMethod().getName(),
                methodInvocation.getMethod().getParameterTypes());

        return methodFromBaseClass.getAnnotation(Metric.class);
    }
}
