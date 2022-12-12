package com.kafka.base;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;

/**
 * @description: 消息消费端
 * @author: YangWanYi
 * @create: 2022-07-04 16:12
 **/
public class MsgConsumer {

    // 主题名
    private final static String TOPIC_NAME = "test-topic";
    // 消费组
    private final static String CONSUMER_GROUP_NAME = "test-group";

    public static void main(String[] args) {
        Properties properties = new Properties();
        // 配置kafka服务端地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.65.60:9999,192.168.65.60:9998");
        // 设置消费分组名
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_NAME);
        /*
            是否自动提交offset 默认是true 不提交offset的话，每次启动consumer都会重复消费。
            一般都设置为false，手动提交。因为自动提交容易出现消息丢失或重复消费的情况。
            比如在自动提交间隔1秒的时间内消费端程序还没执行完就提交了，并且消费端挂了，消息就会丢失；
            或者消费端在0.2秒的时候就已经执行完了后消费端挂了,再次启动就会重复消费。
         */
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 自动提交offset的间隔时间 单位毫秒 ENABLE_AUTO_COMMIT_CONFIG设置为false就不需要设置这个参数
//        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        /*
            配置当消费主题的是一个新的消费组，或者指定offset的消费方式，offset不存在时应该如何消费
            latest：只消费自己启动之后发送到主题的消息（默认的）
            earliest：第一次从头开始消费，以后按照消费offset记录继续消费，这个需要区别于consumer.seekToBeginning(每次都从头开始消费)
         */
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // consumer给broker发送心跳的间隔时间，broker接收到心跳时如果有Rebalance发生，会通过心跳响应把Rebalance方案下发给consumer，这个时间可以稍微短一点。
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);
        // 服务端broker多久感知不到一个consumer心跳就认为这个consumer故障了，会把它踢出消费组。对应的partition也会被重新分配给其他的consumer，默认是10S。
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10 * 1000);
        // 一次poll最大拉取消息的数量 如果消费者处理速度很快，可以设置大点；如果处理速度一般，可以设置小点。
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        /*
            如果两次poll操作间隔超过了这个时间，broker就会认为这个consumer处理能力太弱了，会把它踢出消费组，把分区分配给别的consumer消费。
            如果线上出现莫名其妙不消费被踢出的情况可以排查这个参数，也可以看看处理逻辑是否可以优化性能，或者把MAX_POLL_RECORDS_CONFIG设置小一点。
         */
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 30 * 1000);
        // 反序列化 需要和生产者对应序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties); // 传入配置信息到consumer对象中
        consumer.subscribe(Arrays.asList(TOPIC_NAME)); // 订阅主题 可以批量订阅多个主题 从上次提交的offset开始消费

        // 指定分区消费
//        consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));

        // 消息回溯消费
//        consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
//        consumer.seekToBeginning(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));

        // 指定offset消费
//        consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
//        consumer.seek(new TopicPartition(TOPIC_NAME, 0), 10);

        // 从指定时间点开始消费
//        List<PartitionInfo> partitionInfos = consumer.partitionsFor(TOPIC_NAME);
        /*
            从1小时前开始消费
            没有指定到时间的seek方法，最终都是找到offset偏移量去消费，所以这里其实是根据时间找offset。
         */
//        long fetchDataTime = new Date().getTime() - 1000 * 60 * 60;
//        Map<TopicPartition, Long> map = new HashMap<>();
//        for (PartitionInfo partitionInfo : partitionInfos) {
//            map.put(new TopicPartition(TOPIC_NAME, partitionInfo.partition()), fetchDataTime);
//        }
//        Map<TopicPartition, OffsetAndTimestamp> poMap = consumer.offsetsForTimes(map);
//        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : poMap.entrySet()) {
//            TopicPartition key = entry.getKey();
//            OffsetAndTimestamp value = entry.getValue();
//            if (null == key || null == value) {
//                continue;
//            }
//            long offset = value.offset();
//            System.out.println("partition=" + key.partition() + ",offset=" + offset);
//            // 根据timestamp确定offset
//            consumer.assign(Arrays.asList(key));
//            consumer.seek(key, offset); // 没有指定到时间的seek方法 最终都是找到offset偏移量去消费
//        }

        while (true) {
            // poll()方法是拉取消息的长轮询 如果1S内没有拉到消息会反复拉取 拉到后马上返回
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("收到消息：partition=" + record.partition() + ",offset=" + record.offset() + ",key=" + record.key() + ",value=" + record.value());
            }
            /*
                拉一批消息一次处理完再同步提交 提交的offset会写到broker主题对应的分区中
                这种相对每次消费一条消息就提交一次offset效率要快很多
                相对自动提交也少了很多问题，尽管还可能会出现重复消费的情况，比如这一批消息中只消费了几条客户端就挂掉了，还没来得及提交offset，那么下次启动客户端还会消费那几条数据，导致重复消费。
                但是这种情况关系不大，因为一般核心的业务在消费端会保证它的幂等性。
             */
            if (records.count() > 0) {
                // 手动同步提交offset，当前线程会阻塞直到offset提交成功。一般使用同步提交，因为提交之后一般也没什么业务逻辑了。
                try {
                    consumer.commitSync(); // 相对来说 手动同步提交使用较多
                } catch (Exception e) {
                    consumer.commitSync(); // 提交失败 再次提交
                    e.printStackTrace();
                }

                // 手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的业务逻辑。
               /* consumer.commitAsync((map, e) -> {
                    if (null == e) {
                        System.out.println("offset提交失败：" + map);
                        e.printStackTrace();
                    }
                });*/
            }
        }
    }

}
