package com.tan.homework.micrometer.redis.connection;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * {@link InvocationHandler} {@link RedisConnection}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public class RedisConnectionInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RedisConnectionInvocationHandler.class);

    private final RedisConnection rawRedisConnection;

    private MeterRegistry meterRegistry;

    public RedisConnectionInvocationHandler(RedisConnection rawRedisConnection, MeterRegistry meterRegistry) {
        this.rawRedisConnection = rawRedisConnection;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = method.getName();

        Object invoke = method.invoke(rawRedisConnection, args);

        long responseTime = System.currentTimeMillis() - start;
        if("set".equals(methodName)) {
            Counter redisSetCounter = Counter.builder("redis.set.info")
                    .description("call count for redis set")
                    .register(this.meterRegistry);
            redisSetCounter.increment();

            Timer timer = Timer.builder("redis.set.responseTime")
                    .description("redis set for response time")
                    .register(this.meterRegistry);
            timer.record(responseTime, TimeUnit.MILLISECONDS);
        } else if("get".equals(methodName)) {
            Counter redisGetCounter = Counter.builder("redis.get.info")
                    .description("call count for redis get")
                    .register(this.meterRegistry);
            redisGetCounter.increment();

            Timer timer = Timer.builder("redis.get.responseTime")
                    .description("redis get for response time")
                    .register(this.meterRegistry);
            timer.record(responseTime, TimeUnit.MILLISECONDS);
        }
        return invoke;
    }

}
