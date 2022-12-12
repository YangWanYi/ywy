package com.missy.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: YangWanYi
 * @create: 2022-06-08 12:35
 **/
@Component
public class MissyBeanPostProcessor implements BeanPostProcessor {

    /**
     * 初始化后
     *
     * @param bean
     * @param beanName
     * @return Object
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if ("userService".equals(beanName)) { // 可以通过这种方式实现AOP
//            System.out.println(bean);
            Object proxyInstance = Proxy.newProxyInstance(MissyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
                System.out.println("执行切面逻辑");
                return method.invoke(bean, args);
            });
            return proxyInstance; // 返回代理对象
        }
        return bean;
    }

}
