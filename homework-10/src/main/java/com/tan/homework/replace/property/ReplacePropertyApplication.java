package com.tan.homework.replace.property;

import com.tan.homework.replace.property.listener.EnvironmentPreparedEventListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ReplacePropertyApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ReplacePropertyApplication.class)
                .listeners(new EnvironmentPreparedEventListener())
                .run(args);
    }

}
