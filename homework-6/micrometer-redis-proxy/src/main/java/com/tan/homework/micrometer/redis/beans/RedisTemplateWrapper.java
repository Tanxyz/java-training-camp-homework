package com.tan.homework.micrometer.redis.beans;

import com.tan.homework.micrometer.redis.connection.RedisConnectionInvocationHandler;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Proxy;

/**
 * {@link RedisTemplate} Wrapper class, compatible with {@link RedisTemplate}
 *
 * @param <K> Redis Key type
 * @param <V> Redis Value type
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public class RedisTemplateWrapper<K, V> extends RedisTemplate<K, V> implements MeterBinder {

    private static final Class[] REDIS_CONNECTION_TYPES = new Class[]{RedisConnection.class};

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext context;

    private MeterRegistry meterRegistry;

    private String beanName;

    public RedisTemplateWrapper(RedisTemplate<K, V> redisTemplate, ApplicationContext context) {
        this.context = context;
        initSettings(redisTemplate);
    }

    private void initSettings(RedisTemplate<K, V> redisTemplate) {
        // Set the connection
        setConnectionFactory(redisTemplate.getConnectionFactory());
        setExposeConnection(redisTemplate.isExposeConnection());

        // Set the RedisSerializers
        setEnableDefaultSerializer(redisTemplate.isEnableDefaultSerializer());
        setDefaultSerializer(redisTemplate.getDefaultSerializer());
        setKeySerializer(redisTemplate.getKeySerializer());
        setValueSerializer(redisTemplate.getValueSerializer());
        setHashKeySerializer(redisTemplate.getHashKeySerializer());
        setHashValueSerializer(redisTemplate.getHashValueSerializer());
        setStringSerializer(redisTemplate.getStringSerializer());
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return newProxyRedisConnection(connection);
    }

    protected RedisConnection newProxyRedisConnection(RedisConnection connection) {
        ClassLoader classLoader = this.context.getClassLoader();
        return (RedisConnection) Proxy.newProxyInstance(classLoader, REDIS_CONNECTION_TYPES,
                new RedisConnectionInvocationHandler(connection, this.meterRegistry));
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        this.meterRegistry = registry;
    }
}
