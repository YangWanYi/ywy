package com.example.springbootkafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @description: kafka消费端
 * @author: YangWanYi
 * @create: 2022-07-12 15:53
 **/
//@Component
public class KafkaConsumer {


    /**
     * 消费消息的常用注解[如果在yml中已配置相关参数，这里就不用配置了，如果yml和这里都配置了同样的参数，会优先使用这里的值，如果两处都没有配置，就使用默认值。]
     *
     * @param record
     * @param ack
     * @KafkaListener( groupId="testGroup", // 消费组名
     * topicPartitions={
     * @TopicPartition(topic="topic01",partitions={"0","1"}),
     * @TopicPartition( topic="topic02",
     * partitions="0",
     * partitionOffsets=@PartitionOffset(partition="1",initialOffset="99")
     * )
     * },
     * concurrency="6" // 指同消费组下的消费者个数，也就是并发消费数量，必须小于等于分区总数
     * )
     */
//    @KafkaListener(topics = "test-topic", groupId = "group1")
//    public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        System.out.println(record.value());
//        System.out.println(record);
//        ack.acknowledge(); // 手动提交offset
//    }


    /**
     * 配置多个消费组
     * 一个消费组相当于队列
     * 多个消费组相当于发布订阅
     */
//    @KafkaListener(topics = "test-topic", groupId = "group2")
//    public void listen2Group(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        System.out.println(record.value());
//        System.out.println(record);
//        ack.acknowledge(); // 手动提交offset
//    }
//    @KafkaListener(topics = "TestTopic", groupId = "group1")
//    @KafkaListener(groupId = "group1" ,topicPartitions = {@TopicPartition(topic = "persage.alarm.edge2alarm", partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        System.out.println(record.value());
        System.out.println(record);
        ack.acknowledge(); // 手动提交offset

    }
}
