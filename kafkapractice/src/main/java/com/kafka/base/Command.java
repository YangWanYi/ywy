package com.kafka.base;

/**
 * @description: 指令 用一个简单的实体类举例
 * @author: YangWanYi
 * @create: 2022-07-04 11:19
 **/
public class Command {
    private String name;
    private String detail;

    public Command() {
    }

    public Command(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
