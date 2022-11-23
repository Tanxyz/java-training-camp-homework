package com.tan.homework.micrometer.redis.aspect;

import com.tan.homework.micrometer.redis.metric.RedisMetric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RedisOperateAspect {

    private static final Logger log = LoggerFactory.getLogger(RedisOperateAspect.class);

    @Autowired
    private RedisMetric redisMetric;

    @Pointcut("execution(* com.tan.homework.micrometer.redis.service.RedisOperateService.*(..))")
    public void aspectPoint(){}

    @Around(value = "aspectPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getName(); // 拦截方法名称
        Object result = pjp.proceed();

        long responseTime = System.currentTimeMillis() - start;
        if ("set".equals(methodName)) {
            Counter redisSetCounter = Counter.builder("redis.set.info")
                    .description("call count for redis set")
                    .register(redisMetric.getMeterRegistry());

            Timer timer = Timer.builder("redis.set.responseTime")
                    .description("redis set for response time")
                    .register(redisMetric.getMeterRegistry());
            timer.record(responseTime, TimeUnit.MILLISECONDS);

            redisSetCounter.increment();
        } else if("get".equals(methodName)) {
            Counter redisGetCounter = Counter.builder("redis.get.info")
                    .description("call count for redis get")
                    .register(redisMetric.getMeterRegistry());
            redisGetCounter.increment();

            Timer timer = Timer.builder("redis.get.responseTime")
                    .description("redis get for response time")
                    .register(redisMetric.getMeterRegistry());
            timer.record(responseTime, TimeUnit.MILLISECONDS);
        }
        return result;
    }

}
