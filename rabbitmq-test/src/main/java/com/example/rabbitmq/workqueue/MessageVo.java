package com.example.rabbitmq.workqueue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 消息对象（属性根据业务自定义）
 *
 * @author YangWanYi
 * @version 1.0
 * @date 2022-12-09  16:45
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MessageVo {

    /**
     * 姓名
     */
    private String userName;

    /**
     * 地址
     */
    private String address;

    /**
     * 消息内容
     */
    private String content;
}
