package org.example.actuator.metrics;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

public class MetricSender {

    private final CounterService counterService;

    private final GaugeService gaugeService;

    public MetricSender(CounterService counterService, GaugeService gaugeService) {
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    public void send(MetricName metricName) {
        counterService.increment(metricName.get());
    }

    public void sendElapsedTime(MetricName metricName, long delta) {
        gaugeService.submit("timer." + metricName.get(), delta);
    }

}
