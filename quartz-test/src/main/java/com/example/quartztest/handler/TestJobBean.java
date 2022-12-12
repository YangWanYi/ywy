package com.example.quartztest.handler;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: 定时任务处理器
 * @author: YangWanYi
 * @create: 2022-06-22 16:23
 **/
@Service
public class TestJobBean extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("TestJobBean:"+Thread.currentThread().getName()+"--"+ SimpleDateFormat.getDateTimeInstance().format(new Date()));
    }
}
