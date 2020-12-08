package org.example.actuator.metrics;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

public class MetricHandler implements MethodInterceptor {

    private final MetricSender metricSender;

    private final String ATTEMPTED = ".attempted";
    private final String SUCCEEDED = ".succeeded";
    private final String FAILED = ".failed";
    private final String WARNING = ".warning";
    private final String ELAPSED_TIME = ".elapsedTime";

    public MetricHandler(MetricSender metricSender) {
        this.metricSender = metricSender;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        Metric metric = getMetric(methodInvocation);
        String prefix = metric.prefix();
        Class<? extends Throwable>[] warningExceptions = metric.warningFor();

        try {
            metricSender.send(attempted(prefix));

            stopWatch.start();
            Object result = methodInvocation.proceed();
            metricSender.send(succeeded(prefix));
            return result;

        } catch (Exception e) {
            for (Class<? extends Throwable> warningException : warningExceptions) {
                if (warningException.isAssignableFrom(e.getClass())) {
                    metricSender.send(warning(prefix));
                    throw e;
                }
            }

            metricSender.send(failed(prefix));
            throw e;

        } finally {
            stopWatch.stop();
            metricSender.sendElapsedTime(elapsedTime(prefix), stopWatch.getTotalTimeMillis());
        }

    }

    private Metric getMetric(MethodInvocation methodInvocation) throws NoSuchMethodException {
        Class<? extends Object> baseClass = methodInvocation.getThis().getClass();
        Method methodFromBaseClass = baseClass.getMethod(
                methodInvocation.getMethod().getName(),
                methodInvocation.getMethod().getParameterTypes());

        return methodFromBaseClass.getAnnotation(Metric.class);
    }

    private MetricName attempted(String prefix) {
        return new MetricName(prefix + ATTEMPTED);
    }

    private MetricName succeeded(String prefix) {
        return new MetricName(prefix + SUCCEEDED);
    }

    private MetricName warning(String prefix) {
        return new MetricName(prefix + WARNING);
    }

    private MetricName failed(String prefix) {
        return new MetricName(prefix + FAILED);
    }

    private MetricName elapsedTime(String prefix) {
        return new MetricName(prefix + ELAPSED_TIME);
    }
}
