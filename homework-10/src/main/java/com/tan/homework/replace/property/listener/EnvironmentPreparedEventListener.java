package com.tan.homework.replace.property.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件监听 ApplicationEnvironmentPreparedEvent
 */
public class EnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger log = LoggerFactory.getLogger(EnvironmentPreparedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        PropertiesPropertySource propertySource = (PropertiesPropertySource) propertySources.get(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
        log.info("propertySource - {}", propertySource);
        Map<String, Object> source = new HashMap<>(propertySource.getSource());

        propertySources.replace(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME,
                new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, source));
        PropertySource<?> propertySource1 = propertySources.get(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
        log.info("propertySource - {}", propertySource1);
    }
}
