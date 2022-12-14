package com.tan.homework.bytebuddy.support;

import com.tan.homework.bytebuddy.intercept.UserRegistrationMethodInterceptor;
import com.tan.homework.bytebuddy.service.UserRegistrationService;
import com.tan.homework.bytebuddy.service.impl.InMemoryUserRegistrationService;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().equals(InMemoryUserRegistrationService.class)) {
            try {
                DynamicType.Loaded<Object> proxy = new ByteBuddy()
                    .subclass(Object.class)
                    //实现接口
                    .implement(UserRegistrationService.class)
                    //.method(ElementMatchers.named("registerUser"))
                    .method(ElementMatchers.isDeclaredBy(UserRegistrationService.class))
                    .intercept(MethodDelegation.to(new UserRegistrationMethodInterceptor((InMemoryUserRegistrationService)bean)))
                    .make()
                    .load(UserRegistrationService.class.getClassLoader());

                // 保存到磁盘
                //proxy.saveIn(new File("D:\\temp\\"));
                return (UserRegistrationService) proxy.getLoaded().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                // do nothing
            }
        }
        return bean;
    }
}
