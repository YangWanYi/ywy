package com.example.utils.transfer;

/**
 * @description: 字符串转换工具
 * @author: YangWanYi
 * @create: 2022-08-02 09:56
 **/
public class UtilString {

    /**
     * 16进制转换为字符串
     * 如果调用时显示常量过长，请按如下操作：
     * File -> Settings -> Build,Execution,Deployment -> Compiler -> Java Compiler ， Use Compiler, 选择Eclipse , 点击 Apply
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String getFileSize(String size) {
        double length = Double.parseDouble(size);
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (length < 1024) {
            return length + "B";
        } else {
            length = length / 1024.0;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (length < 1024) {
            return Math.round(length * 100) / 100.0 + "KB";
        } else {
            length = length / 1024.0;
        }
        if (length < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            return Math.round(length * 100) / 100.0 + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            return Math.round(length / 1024 * 100) / 100.0 + "GB";
        }
    }

}
