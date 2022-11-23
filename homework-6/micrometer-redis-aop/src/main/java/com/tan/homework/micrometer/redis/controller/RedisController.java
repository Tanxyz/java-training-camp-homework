package com.tan.homework.micrometer.redis.controller;

import com.tan.homework.micrometer.redis.service.RedisOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisOperateService redisOperateService;

    @GetMapping("/set")
    public String redisSet(@RequestParam("total") Integer total) {
        for(int i = 0; i < total; i++) {
            redisOperateService.set("key_" + i, "val_" + i);
        }
        return "Success";
    }

}
