//package com.example.quartztest.scoket;
//
//import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft;
//
//import javax.net.ssl.*;
//import java.net.URI;
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//import java.util.Map;
//
//public abstract class WebScoketClientPlus extends WebSocketClient {
//    public WebScoketClientPlus(URI serverURI, Draft draft, Map<String, String> headers, int connecttimeout) {
//        super(serverURI, draft, headers, connecttimeout);
//        /**
//         * 如果url包含wss，添加wss协议支持
//         */
//        if (serverURI.toString().contains("wss://")) {
//            trustAllHosts(this);
//        }
//    }
//
//    /**
//     * 扩展方法：目前不做扩展
//     */
//    public abstract void extendMethod();
//
//    public static void trustAllHosts(WebScoketClientPlus appClient) {
//        System.out.println("start...");
//        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[0];
//            }
//
//            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
//            }
//
//            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
//            }
//        }};
//        try {
//            /**
//             * 添加wss支持
//             */
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init((KeyManager[]) null, trustAllCerts, new SecureRandom());
//            appClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
//        } catch (Exception var3) {
//            var3.printStackTrace();
//        }
//    }
//}
//
