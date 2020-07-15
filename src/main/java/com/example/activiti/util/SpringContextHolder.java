package com.example.activiti.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author Lv Jie
 * @version 1.0
 * @description TODO
 * @date 2020-07-15 8:55
 */

@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(SpringContextHolder.class);
    private static ApplicationContext applicationContext = null;

    public SpringContextHolder() {
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static void clearHolder() {
        if (log.isDebugEnabled()) {
            log.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        }

        applicationContext = null;
    }

    public static void publishEvent(ApplicationEvent event) {
        if (applicationContext != null) {
            applicationContext.publishEvent(event);
        }
    }

    public void destroy() {
        clearHolder();
    }
}
