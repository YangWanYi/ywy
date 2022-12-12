package com.spring;

public interface BeanPostProcessor {

    /**
     * 初始化前
     *
     * @param bean
     * @param beanName
     * @return Object
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后
     *
     * @param bean
     * @param beanName
     * @return Object
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
