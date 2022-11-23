package com.tan.homework.micrometer.redis.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

@Component
public class RedisMetric implements MeterBinder {

    private MeterRegistry meterRegistry;

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
