package org.example.actuator.metrics;

import io.micrometer.core.instrument.MeterRegistry;

enum MetricTypeSender {
    ATTEMPTED("attempted"),
    SUCCEEDED("succeeded"),
    FAILED("failed"),
    WARNING("warning");

    MetricTypeSender(String suffix) {
        this.suffix = suffix;
    }

    private String suffix;

    public void increment(String prefix, MeterRegistry meterRegistry) {
        meterRegistry.counter(nameOfMetric(prefix)).increment();
    }

    private String nameOfMetric(String prefix) {
        return String.format("%s.%s", prefix, this.suffix);
    }
}
