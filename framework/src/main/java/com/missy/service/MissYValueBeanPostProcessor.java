package com.missy.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;
import com.spring.MissYName;

import java.lang.reflect.Field;

/**
 * @description:
 * @author: YangWanYi
 * @create: 2022-06-08 13:22
 **/
@Component
public class MissYValueBeanPostProcessor implements BeanPostProcessor {

    /**
     * 初始化前
     *
     * @param bean
     * @param beanName
     * @return Object
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        for (Field field : bean.getClass().getDeclaredFields()) { // 遍历类的属性
            if (field.isAnnotationPresent(MissYName.class)) { // 属性是否有注解MissYName
                field.setAccessible(true); // 反射
                try {
                    field.set(bean, field.getAnnotation(MissYName.class).value()); // 注解的值注入
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
