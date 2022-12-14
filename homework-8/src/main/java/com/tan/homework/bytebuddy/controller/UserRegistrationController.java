package com.tan.homework.bytebuddy.controller;

import com.tan.homework.bytebuddy.domain.User;
import com.tan.homework.bytebuddy.service.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册控制器
 */
@RestController
public class UserRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationController.class);

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping(value = "/user/register")
    public Boolean registerUser(@RequestBody User user) {
        log.info("registerUser : {}", userRegistrationService);
        return userRegistrationService.registerUser(user);
    }

    @PostMapping(value = "/user/destory")
    public Boolean destoryUser(@RequestBody User user) {
        return userRegistrationService.destoryUser(user.getId());
    }

}
