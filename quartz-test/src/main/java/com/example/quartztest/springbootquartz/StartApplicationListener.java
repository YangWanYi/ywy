package com.example.quartztest.springbootquartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @description: springboot容器启动监听 在这个监听中自动启动任务调度
 * @author: YangWanYi
 * @create: 2022-06-25 16:41
 **/
@Component
public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent> { // 监听容器启动事件 当spring发布ContextRefreshedEvent这个事件时，表示spring的IOC容器已经启动成功了

    // 注入调度器
    @Qualifier("Scheduler")
    @Autowired
    private Scheduler scheduler;

    /**
     * 在这个监听方法中开启调度
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger01", "group01");
        try {
            Trigger trigger = this.scheduler.getTrigger(triggerKey); // 在调度器中获取触发器
            if (null == trigger) { // 如果调度器中没有触发器再new一个 这样是为了保证触发器在调度器中是唯一的 因为同一个策略的触发器只需要一个实例就够了
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey) // 设置key
                        .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")) // cron表达式 10S执行一次
                        .build();
                JobDetail jobDetail = JobBuilder.newJob(MyQuartzJob.class) // 把定时任务传进去
                        .withIdentity("job01", "group01") // 设置key
                        .build();
                this.scheduler.scheduleJob(jobDetail, trigger);
                this.scheduler.start(); // 开始调度
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
