package com.example.quartztest.pool;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: 定时任务线程池 对timer的缺陷做了些优化
 * @author: YangWanYi
 * @create: 2022-06-23 18:06
 **/
public class ScheduleThreadPoolTest {

    public static void main(String[] args) {
        /*
            ScheduledThreadPoolExecutor的特点：
            1、使用多线程执行任务，不会相互阻塞
            2、如果线程失活，会新建线程执行任务（线程抛异常，任务会被丢弃，需要做捕获处理）
            3、DelayedWorkQueue:小顶堆、无界队列
                ①、在定时线程池中，最大线程数是没有意义的
                ②、执行时间距离当前时间越近的任务在队列的前面
                ③、用于添加ScheduleFutureTask（继承自FutureTask，实现RunnableScheduledFuture接口），提供异步执行的能力，并且可以返回执行结果
                ④、线程池中的线程从DelayQueue中获取ScheduleFutureTask，然后执行任务
                ⑤、实现了Delayed接口，可以通过getDelay方法获取延迟时间
            4、Leader-Follower模式：
                解释：譬如现在有一堆等待执行的任务（一般待执行的任务是存放在一个队列中排好序），而所有的工作线程中只会有一个是leader线程，其他的都是follower线程。
                只有leader线程能执行任务，而剩下的follower线程则处于休眠状态。当leader线程在拿到任务后执行任务前，就会变成follower线程，同时会选出一个新的leader线程。
                如果此时有下一个任务，就是这个新的leader线程来执行了，并往复这个过程。
                当之前那个执行任务的线程执行完毕时，会判断如果此时已经没有任务了或者有任务但是其他的线程是leader线程，自己就会休眠。如果此时有任务但是没有leader线程，那自己就会重新成为leader线程来执行任务。
                好处：避免没必要的唤醒和阻塞，这样更加有效且节省资源
            5、应用场景：适用于多个后台线程执行周期性任务，同时为了满足资源管理的需求而限制后台线程数量

            SingleThreadScheduledExecutor的特点：
            1、相当于单线程的ScheduledThreadPoolExecutor
            2、应用场景：适用于需要单个后台线程执行周期任务，同时需要保证任务顺序执行
         */
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);// 定义5个线程数
        for (int i = 0; i < 2; i++) {
//            scheduledThreadPool.schedule(new Task("task-" + i), 0, TimeUnit.SECONDS); // 延迟时间为0 表示立即执行 没有间隔时间 表示只执行一次
            scheduledThreadPool.scheduleAtFixedRate(new Task("task-" + i), 0, 1000 * 2, TimeUnit.SECONDS); // 延迟时间为0 表示立即执行 执行间隔时间2S
        }
    }

}

/**
 * 定义任务逻辑
 */
class Task implements Runnable {
    // 任务名称
    String taskName;

    public Task(String taskName) {
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