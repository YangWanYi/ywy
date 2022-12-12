package com.example.utils;

import com.example.utils.transfer.UtilString;

import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: YangWanYi
 * @create: 2022-08-02 09:58
 **/
public class Test {
    public static void main(String[] args) {
//        StringBuilder builder = new StringBuilder();
//        String info = builder.append("").append("").append("").append("").append("").toString();
        String info = "";
        System.out.println(info.getBytes(StandardCharsets.UTF_8).length);
        System.out.println(UtilString.getFileSize(String.valueOf(info.getBytes(StandardCharsets.UTF_8).length)));
        System.out.println(UtilString.hexStringToString(info));
    }
}
