package com.findme.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class ApplicationCtxHolderUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);

    @Override
    public synchronized void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {

        logger.info("[CTX UTIL] - Init application context.");

        ApplicationCtxHolderUtil.context = context;
    }

    public static ApplicationContext getContext() {
        return ApplicationCtxHolderUtil.context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getContext().getBean(clazz);
    }
}
