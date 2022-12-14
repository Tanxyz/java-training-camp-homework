package com.tan.homework.bytebuddy.intercept;

import com.tan.homework.bytebuddy.domain.User;
import com.tan.homework.bytebuddy.service.impl.InMemoryUserRegistrationService;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class UserRegistrationMethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationMethodInterceptor.class);

    public InMemoryUserRegistrationService userRegistrationService;

    public UserRegistrationMethodInterceptor(InMemoryUserRegistrationService userRegistrationService){
        this.userRegistrationService  = userRegistrationService;
    }

    @RuntimeType
    public Object interceptor(@Origin Method method , @AllArguments Object[] args) throws Exception {
        Object result = method.invoke(this.userRegistrationService, args);
        if(method.getName().equals("registerUser")) {
            User user = (User) args[0];
            log.info("用户:{}注册成功", user.getName());
        } else if(method.getName().equals("destoryUser")) {
            Long userId = (Long) args[0];
            log.info("用户:{}注销成功", userId);
        }
        return result;
    }

}
