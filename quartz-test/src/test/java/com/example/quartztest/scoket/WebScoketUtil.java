//package com.example.quartztest.scoket;
//
//import lombok.Data;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.java_websocket.WebSocket.Role;
//import org.java_websocket.drafts.Draft;
//import org.java_websocket.drafts.Draft_17;
//import org.java_websocket.handshake.ServerHandshake;
//import org.springframework.stereotype.Component;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.nio.ByteBuffer;
//import java.nio.CharBuffer;
//import java.nio.charset.CharacterCodingException;
//import java.nio.charset.Charset;
//import java.nio.charset.CharsetDecoder;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//@Slf4j
//@Data
//@Component
//public class WebScoketUtil {
//    public static WebScoketClientPlus getConnect(String url, CountDownLatch countDownLatch) throws URISyntaxException {
//        Map<String, String> headers = new HashMap();
//        /**
//         * 指定webScoket的请求头信息
//         */
//        headers.put("Sec-WebSocket-Extensions", "permessage-deflate; client_max_window_bits");
//        headers.put("Sec-WebSocket-Version", "13");
//        headers.put("Connection", "Upgrade");
//        headers.put("Upgrade", "websocket");
//        headers.put("Accept-Encoding", "gzip, deflate, br");
//        headers.put("Accept-Language", "zh-CN,zh;q=0.9");
//        headers.put("Cache-Control", "no-cache");
//        /**
//         * 指定角色为客户端
//         */
//        Draft draft = new Draft_17();
//        draft.setParseMode(Role.CLIENT);
//        WebScoketClientPlus connect = new WebScoketClientPlus(new URI(url), draft, headers, 10) {
//
//
//            @Override
//            public void extendMethod() {
//                System.err.println("extendMethod");
//            }
//
//            @SneakyThrows
//            public void onClose(int arg0, String arg1, boolean arg2) {
//                System.err.println("onClose");
//            }
//
//            public void onError(Exception arg0) {
//                System.err.println("onError");
//            }
//
//            public void onMessage(String arg0) {
//                System.out.println("String 消息：" + arg0);
//            }
//
//            public void onOpen(ServerHandshake arg0) {
//                countDownLatch.countDown();
//                System.out.println("onOpen：" + arg0);
//            }
//
//            public void onMessage(ByteBuffer bytes) {
//                Charset charset = Charset.forName("UTF-8");
//                CharsetDecoder decoder = charset.newDecoder();
//                CharBuffer charBuffer = null;
//                String received = "";
//                try {
//                    charBuffer = decoder.decode(bytes);
//                } catch (CharacterCodingException e) {
//                    e.printStackTrace();
//                }
//                bytes.flip();
//                received = charBuffer.toString();
//                System.out.println("接收到的流式数据：" + received);
//            }
//        };
//        connect.connect();
//        return connect;
//    }
//}
//
