package com.spring;

/**
 * @description:bean定义
 * @author: YangWanYi
 * @create: 2022-06-08 10:57
 **/
@Component
public class BeanDefinition {

    // bean类型
    private Class type;

    // bean作用域：单例或原型
    private String scope;

    // bean是否懒加载
    private boolean isLazy;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
