package com.kafka.base;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @description: 消息生产者
 * @author: YangWanYi
 * @create: 2022-07-04 10:19
 **/
public class MsgProducer {

    // 主题名
//    private final static String TOPIC_NAME = "TestTopic";
    private final static String TOPIC_NAME = "persage.alarm.alarm2edge";

    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        // 配置kafka服务端地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "113.108.52.72:4439,113.108.52.72:4440,113.108.52.72:4441");
        /*
            设置发出消息的持久化机制 默认是1
            acks=0：表示producer不需要等待任何broker确认收到消息的回复，就可以继续发送下一条消息。性能最高，但最容易丢失消息；
            acks=1：至少要等待leader已经成功把数据写入本地log，但是不需要等待所有followers成功写入，就可以继续发送下一条消息。这种情况下，如果follower没有成功备份数据，而此时leader又恰好挂掉了，消息就会丢失；
            acks=-1或all：需要等待min.insync.replicas(默认是1，推荐配置大于等于2)这个参数配置的副本个数都成功写入日志，才可以继续发送下一条消息。这种策略只要有一个备份存活就不会丢失数据。这是最强的数据保证，一般除非金融级别，或者跟钱有关的场景才会使用这种配置。
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        // 消息发送失败会重试，默认重试间隔100ms，重试能保证消息发送的可靠性，但是也可能造成消息重复发送，比如网络抖动的情况，所以需要在接收者（消费者）那边做好消息接收的幂等性处理。
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 重试间隔设置 单位毫秒
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 300);
        // 设置发送消息的本地缓冲区 如果设置了该缓冲区，消息会先发送到本地缓冲区，可以提高消息发送性能，默认值是33554432，即32MB。
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // kafka本地线程会从缓冲区取数据，批量发送到broker。设置批量发送消息的大小，默认是16384，即16KB，表示一个batch满了16KB就发送出去。
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        /*
            默认值是0，表示消息必须立即被发送，这会影响性能。
            一般设置10毫秒左右，就是说这个消息发送完后会进入本地的一个batch，如果10ms内这个batch满了16KB就会随batch一起被发送出去；
            如果10ms内batch没满，那也要把消息发送出去，不能让消息的发送延迟时间太长。
         */
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        // 把发送的key从字符串序列化为字节数组
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 把发送的value从字符串序列化为字节数组
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(properties); // 传入配置信息到producer对象中
        int msgNum = 5;
        final CountDownLatch countDownLatch = new CountDownLatch(msgNum); // 指定CountDownLatch的数量 和需要发送消息的数量一致
        for (int i = 0; i < msgNum; i++) {
            Command command = new Command("testName" + i, "detail"); // 构建自定义的消息对象
            // 指定发送主题和分区
//            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, 0, command.getName(), JSON.toJSONString(command));
            // 仅指定主题不指定分区 具体发送的分区计算方式：hash(key)%partitionNum  Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, command.getName(), JSON.toJSONString(command));
            // 等待消息发送成功的同步阻塞方法(send方法都是异步的 这里通过get方法同步)
            /*RecordMetadata recordMetadata = producer.send(producerRecord).get();
            System.out.println("同步方式发送消息的结果：" + "topic-" + recordMetadata.topic() + ",partition-" + recordMetadata.partition() + ",offset-" + recordMetadata.offset());*/
            // 异步回调方式发送消息
            producer.send(producerRecord, (recordMetadata, e) -> {
                if (null != e) {
                    System.out.println("消息发送失败：" + e.getStackTrace());
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                if (null != recordMetadata) {
                    System.out.println("异步方式发送消息的结果：" + "topic-" + recordMetadata.topic() + ",partition-" + recordMetadata.partition() + ",offset-" + recordMetadata.offset());
                }
                countDownLatch.countDown(); // 发送成功就CountDownLatch就减去1
            });
            // TODO 处理业务逻辑  如果有业务逻辑建议使用异步 如果没有业务逻辑 可以使用同步
        }
        countDownLatch.await(5, TimeUnit.SECONDS); // 保证所有消息都收到成功通知才释放资源
        producer.close(); // 关掉生产者对象 释放资源
    }
}
