package com.tan.homework.bytebuddy.service;

import com.tan.homework.bytebuddy.domain.User;

/**
 * 用户注册服务接口
 * @author Tan
 */
public interface UserRegistrationService {

    /**
     * 注册
     * @param user
     * @return
     */
    Boolean registerUser(User user);

    /**
     * 注销
     * @param id
     * @return
     */
    Boolean destoryUser(Long id);

}
