package com.example.quartztest.springbootquartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @description: 任务类 处理任务逻辑
 * @author: YangWanYi
 * @create: 2022-06-25 15:52
 **/
// 禁止并发地执行同一个Job（JobDetail）定义的多个实例
@DisallowConcurrentExecution
/*
    把jobDataMap持久化 哪怕每次schedule创建不同的jobDetail实例 jobDetail值都会更新 对trigger的dataMap无效
    如果一个任务不是持久化的，即没有加这个注解，那么当没有触发器关联这个任务时，Quartz就会从Schedule中删除这个任务。
 */
@PersistJobDataAfterExecution
public class MyQuartzJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        try {
            Thread.sleep(1000 * 2);
            System.out.println(jobExecutionContext.getScheduler().getSchedulerInstanceId()); // 打印调度器的实例ID
            System.out.println("任务名称：" + jobExecutionContext.getJobDetail().getKey().getName()); // 打印任务名称
            System.out.println("任务执行时间：" + new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
