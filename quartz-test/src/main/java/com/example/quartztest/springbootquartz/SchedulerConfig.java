package com.example.quartztest.springbootquartz;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @description: 调度器配置类 调度器实例只要一个就够了 所以需要建一个配置类把调度器放在IOC容器中 作为一个bean保存起来
 * @author: YangWanYi
 * @create: 2022-06-25 16:01
 **/
@Configuration
public class SchedulerConfig {

    @Autowired
    private DataSource dataSource;

    @Bean("Scheduler")
    public Scheduler getScheduler() throws IOException {
        return this.getSchedulerFactoryBean().getScheduler();
    }

    @Bean
    public SchedulerFactoryBean getSchedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName("cluster_scheduler"); // 调度器名称 不设置也可以
        factory.setDataSource(dataSource); // 设置数据源
        factory.setApplicationContextSchedulerContextKey("application"); // 设置SchedulerFactoryBean在IOC中的key 可以不设置
        factory.setQuartzProperties(this.getQuartzProperties()); // 设置配置文件
        factory.setTaskExecutor(this.getSchedulerThreadPool()); // 设置任务执行器 线程池
        factory.setStartupDelay(5); // 设置执行延迟时间 不设置就是立即执行
        return factory;
    }

    @Bean
    public Properties getQuartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/spring-quartz.properties")); // 设置文件路径
        propertiesFactoryBean.afterPropertiesSet(); // 调用此方法才会真正地读取配置文件
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public Executor getSchedulerThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors()); // 设置核心线程数为处理器的核心数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors()); // 设置最大线程数
        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors()); // 设置队列容量
        return executor;
    }
}
