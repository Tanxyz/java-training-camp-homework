package com.tan.homework.bytebuddy.service.impl;

import com.tan.homework.bytebuddy.domain.User;
import com.tan.homework.bytebuddy.service.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实现 UserRegistrationService
 */
@Service("userRegistrationService")
public class InMemoryUserRegistrationService implements UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserRegistrationService.class);

    private Map<Long, User> usersCache = new ConcurrentHashMap<>();

    @Autowired
    private Tracer tracer;

    @Autowired
    private CurrentTraceContext currentTraceContext;

    @Override
    public Boolean registerUser(User user) {
        Long id = user.getId();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Span initialSpan = (Span) request.getAttribute(Span.class.getName());
        Span newSpan = null;
        Boolean success = false;
        try (Tracer.SpanInScope ws = this.tracer.withSpan(initialSpan)) {
            newSpan = this.tracer.nextSpan().name("userRegistrationService");
            success = usersCache.putIfAbsent(id, user) == null;
            logger.info("registerUser() == {}", success);
        } finally {
            if (newSpan != null) {
                newSpan.end();
            }
        }
        return success;
    }

    @Override
    public Boolean destoryUser(Long id) {
        User user = usersCache.remove(id);
        if(user == null) {
            logger.info("用户注销该用户:{}不存在或已注销", id);
            return false;
        }
        logger.info("用户:{}注销成功", user.getName());
        return true;
    }
}
