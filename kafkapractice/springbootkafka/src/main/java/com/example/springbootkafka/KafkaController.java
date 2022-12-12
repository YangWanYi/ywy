package com.example.springbootkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: kafka测试接口
 * @author: YangWanYi
 * @create: 2022-07-11 14:57
 **/
@RestController
public class KafkaController {

    private final static String TOPIC_NAME = "TestTopic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg") String msg) {
        // 更多API进入KafkaTemplate对象查看
        kafkaTemplate.send(TOPIC_NAME, msg).addCallback(success -> System.out.println("消息发送成功:" + success.getRecordMetadata().topic() + "-" + success.getRecordMetadata().partition() + "-" + success.getRecordMetadata().offset() + "-" + msg),
                failure -> System.out.println("消息发送失败：" + failure.getMessage()));
    }

}
