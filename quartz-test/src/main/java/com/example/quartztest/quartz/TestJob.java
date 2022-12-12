package com.example.quartztest.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @description: 测试定时任务
 * @author: YangWanYi
 * @create: 2022-06-24 14:20
 **/
public class TestJob {

    public static void main(String[] args) {
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("jobName01", "groupName01") // 在调度器中任务名称不能重复，必须唯一
                .usingJobData("job", "jobDetail") // 存值
                .usingJobData("name", "jobDetail") // 存值
                .usingJobData("jobDetailCount", 0) // 存值
                .build();

        // 创建触发器
        Trigger trigger = TriggerBuilder. newTrigger()
                .withIdentity("triggerName01", "group01")
                .usingJobData("trigger", "trigger") // 存值
                .usingJobData("name", "trigger") // 存值
                .usingJobData("count", 0) // 存值
                .startNow() // 立即启动
                .withSchedule( // 触发策略
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1) // 间隔1S执行
                                .repeatForever()) // 一直重复执行
                .build();

        // 创建调度器
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); // 获取默认的schedule
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start(); // 启动
        } catch (SchedulerException e) {
            e.printStackTrace();
            System.out.println("调度器创建失败");
        }

    }

}
