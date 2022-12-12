package com.example.quartztest.config;

import com.example.quartztest.handler.TestJobBean;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 定时任务相关配置
 * @author: YangWanYi
 * @create: 2022-06-22 16:22
 **/
@Configuration
public class CustomizeScheduleConfigTask {

    /**
     * 定时任务
     *
     * @return
     */
    @Bean
    public JobDetail testJobDetail() {
        return JobBuilder.newJob(TestJobBean.class).withIdentity("testJobDetail").storeDurably().build();
    }


    /**
     * 触发器
     *
     * @param jobDetail
     * @return
     */
    @Bean
    public Trigger testJobTrigger(@Qualifier("testJobDetail") JobDetail jobDetail) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever();
        return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity("testJobTrigger").withSchedule(simpleScheduleBuilder).build();
    }

}
