package org.example.actuator.metrics;

public class MetricName {

    private String name;

    public MetricName(String name) {
        this.name = name;
    }

    public String get() {
        return name;
    }

}
