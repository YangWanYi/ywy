package com.example.ribbonpractice.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RetryRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 主要配置
 * @author: YangWanYi
 * @create: 2021-10-20 11:42
 **/
@Configuration
public class MainConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /**
     * 设置负载均衡的策略 默认是轮训
     * @return IRule
     */
    @Bean
    public IRule setRule(){
        return new RandomRule(); // 随机
//        return new RetryRule(); // 重试
    }
}
