import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 消费端测试
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-11  00:32
 */
public class ConsumerTest {
    public static void main(String[] args) {
        // 初始化IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-rabbitmq-consumer.xml");
    }
}
