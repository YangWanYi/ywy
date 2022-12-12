package com.missy;

import com.missy.service.OrderService;
import com.missy.service.UserInterface;
import com.missy.service.UserService;
import com.spring.MissyApplicationContext;

/**
 * @description: 测试
 * @author: YangWanYi
 * @create: 2022-06-08 10:19
 **/
public class Test {

    public static void main(String[] args) {

        // 扫描->创建到单例bean
        MissyApplicationContext missyApplicationContext = new MissyApplicationContext(AppConfig.class);
//        System.out.println(missyApplicationContext.getBean("userService"));
//        System.out.println(missyApplicationContext.getBean("userService"));
//        System.out.println(missyApplicationContext.getBean("orderService"));
        UserInterface userService = (UserInterface) missyApplicationContext.getBean("userService");
        userService.test();
//        OrderService orderService = (OrderService) missyApplicationContext.getBean("orderService");
//        orderService.sayYes();
    }

}
