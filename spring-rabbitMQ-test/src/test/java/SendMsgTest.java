import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 发送消息测试
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-10  23:49
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class SendMsgTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    public void testHelloWorld() {
        // 简单模式 发消息
        rabbitTemplate.convertAndSend("spring_queue", "hello world spring....");
    }

    @Test
    public void testFanout() {
        // 队列模式 发消息
        rabbitTemplate.convertAndSend("spring_fanout_exchange", "", "spring fanout....");
    }

    @Test
    public void testDirect() {
        // 路由模式 发消息
        rabbitTemplate.convertAndSend("spring_direct_exchange", "info", "spring Direct....");
    }

    @Test
    public void testTopics() {
        // 主题模式 发消息
        rabbitTemplate.convertAndSend("spring_topic_exchange", "baiqi.hehe.haha", "spring topic....");
    }
}
