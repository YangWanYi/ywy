package com.missy.service;

import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.MissYName;
import com.spring.Scope;

/**
 * @description:
 * @author: YangWanYi
 * @create: 2022-06-08 10:20
 **/
@Component("userService")
@Scope("prototype")
//public class UserService implements InitializingBean, UserInterface {
public class UserService implements UserInterface, BeanNameAware {

//    @Autowired
//    private OrderService orderService;

    @MissYName("ywy")
    private String nameOfMissY;

    private String beanName;

    @Override
    public void test() {
        System.out.println(beanName);
        System.out.println("一切为了更好地生活");
        System.out.println(nameOfMissY);
//        System.out.println(orderService);
    }

    /**
     * 回调 获取beanName
     *
     * @param beanName
     */
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

//    @Override
//    public void afterPropertiesSet() {
////        System.out.println("bean初始化");
//    }
}
