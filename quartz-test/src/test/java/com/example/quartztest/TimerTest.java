package com.example.quartztest;

import java.util.Date;
import java.util.Timer;

/**
 * @description: 定时任务timer使用演示
 * @author: YangWanYi
 * @create: 2022-06-23 17:32
 **/
public class TimerTest {

    public static void main(String[] args) {
        // timer执行任务调度是基于绝对时间的，做不到每周一这样的时间执行，而且运行时异常会导致timer线程终止
        Timer timer = new Timer(); // 创建一个timer对象 这时任务就已经启动了
        for (int i = 0; i < 2; i++) {
            TimerTask timerTask = new TimerTask("timeTask" + i); // TimerTask以小顶堆的方式存放在TaskQueue中
            /*
                schedule 真正的执行时间取决于上一个任务的结束时间 这种很有可能会丢任务 即该执行的时间没执行
                scheduleAtFixedRate 严格按照预设时间执行 不管上一次任务是否执行结束 这种执行时间会乱
                上面的问题是因为单线程任务阻塞导致的，可以在任务实现类中用线程池执行任务来解决
             */
//            timer.schedule(timerTask, new Date(), 1000 * 2); // 添加任务 这里设定任务距离上一次执行完毕后间隔2S再次执行
            timer.scheduleAtFixedRate(timerTask, new Date(), 1000 * 2); // 添加任务 这里设定任务不管上一次是否执行结束 距离上一次开始执行时间2S后都会再次执行
        }
    }

}


/**
 * 定时任务类
 */
class TimerTask extends java.util.TimerTask {

    // 任务名称
    String taskName;

    public TimerTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        try {
            System.out.println("taskName:" + taskName + ",startTime:" + new Date());
            Thread.sleep(1000 * 3); // 任务执行3S
            System.out.println("taskName:" + taskName + ",endTime:" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
