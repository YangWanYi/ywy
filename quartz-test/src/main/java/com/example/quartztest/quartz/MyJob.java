package com.example.quartztest.quartz;

import org.quartz.*;

import java.util.Date;

/**
 * @description: 定时任务的业务逻辑
 * @author: YangWanYi
 * @create: 2022-06-24 14:18
 **/
@DisallowConcurrentExecution // 禁止并发地执行同一个Job（JobDetail）定义的多个实例
/*
    把jobDataMap持久化 哪怕每次schedule创建不同的jobDetail实例 jobDetail值都会更新 对trigger的dataMap无效
    如果一个任务不是持久化的，即没有加这个注解，那么当没有触发器关联这个任务时，Quartz就会从Schedule中删除这个任务。
 */
@PersistJobDataAfterExecution
public class MyJob implements Job {

    /**
     * 需要保证变量名称和JobDataMap的key一致 且有set方法 才可以拿到值
     * 如果jobDetail和trigger都设置了这个属性 jobDetail的值会被trigger覆盖
     */
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 执行任务的逻辑
     *
     * @param jobExecutionContext 定时任务的容器 提供各种API访问全局的东西
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("我的定时任务，执行时间：" + new Date());
        System.out.println("name：" + this.name); // 如果jobDetail和trigger都设置了这个属性 jobDetail的值会被trigger覆盖
        // 这里可以获取创建任务或触发器时存储的数据
        JobDataMap jobDetailMap = jobExecutionContext.getJobDetail().getJobDataMap(); // 取JobDetail存的值
        JobDataMap triggerMap = jobExecutionContext.getTrigger().getJobDataMap(); // 获取Trigger存的值
        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap(); // 获取JobDetail和Trigger合并后存的值 如果key一样可能会被覆盖
        triggerMap.put("count", triggerMap.getInt("count") + 1);
        jobDetailMap.put("jobDetailCount", jobDetailMap.getInt("jobDetailCount") + 1);

        System.out.println("jobDetailMap-job:" + jobDetailMap.getString("job"));
        System.out.println("jobDetailMap-jobDetailCount:" + jobDetailMap.getInt("jobDetailCount")); // 加了注解@PersistJobDataAfterExecution后会累加 否则不会
        System.out.println("triggerMap-trigger:" + triggerMap.getString("trigger"));
        System.out.println("triggerMap-count:" + triggerMap.getInt("count")); // 加了注解@PersistJobDataAfterExecution后也不会累加
        System.out.println("mergedJobDataMap-trigger:" + mergedJobDataMap.getString("trigger"));
        System.out.println("mergedJobDataMap-job:" + mergedJobDataMap.getString("job"));

        /*
            Schedule每次执行，都会根据jobDetail创建一个新的Job实例，这样就可以规避并发访问的问题，JobDetail的实例也是新创建的。
            Quartz定时任务默认都是并发执行的，不会等待上一次任务执行完毕，只要间隔时间到就会执行，如果定时任务执行时间太长，会长时间占用资源，导致其他任务阻塞。
            @DisallowConcurrentExecution注解放在Job类上，可以禁止并发地执行同一个Job（JobDetail）定义的多个实例。
         */
        System.out.println("jobDetail:" + System.identityHashCode(jobExecutionContext.getJobDetail()));
        System.out.println("job:" + System.identityHashCode(jobExecutionContext.getJobInstance()));

        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
