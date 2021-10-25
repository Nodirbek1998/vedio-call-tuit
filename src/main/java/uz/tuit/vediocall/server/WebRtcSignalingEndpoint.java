package uz.tuit.vediocall.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketSession;
import uz.tuit.vediocall.config.Users;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author Mamadaliyev Nodirbek
 * @created 10/5/21 - 10:08 AM
 */
//@Controller
//@ServerEndpoint("/msgServer/{userId}")
//@Component
//@Scope("prototype")
//public class WebSocketServer {
//
//    /**
//     * Static variable that records the current number of online connections.It should be designed to be thread safe.
//     */
//    private static int onlineCount = 0;
//    /**
//     * concurrent The thread-safe Set of the package that holds the MyWebSocket object for each client.
//     */
//    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();
//    /**
//     * A connection session with a client through which data is sent to the client
//     */
//    private Session session;
//    /**
//     * Receive userId
//     */
//    private String userId = "";
//
//    @Autowired
//    private UsersServer userServer;
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("userId") String userId) {
//        this.session = session;
//        this.userId = userId;
//        /**
//         * Connection opened: add session to socket-map
//         */
//        webSocketMap.put(userId, session);
//        System.out.println(userId + " - Connection established successfully...");
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        try {
//            Enumeration<String> keys =webSocketMap.keys();
//            System.out.println("Messages received by the server:"+message);
//
//            while(keys.hasMoreElements()) {
//                String key = keys.nextElement();
//                //Determine if the user is still online
//                if (webSocketMap.get(key) == null){
//                    webSocketMap.remove(key);
//                    System.err.println(key + " : null");
//                    continue;
//                }
//                Session sessionValue = webSocketMap.get(key);
//                //Remove Forwarding to Yourself
//                if (key.equals(this.userId)){
//                    System.err.println("my id " + key);
//                    continue;
//                }
//                //Determine if session is open
//                if (sessionValue.isOpen()){
//                    sessionValue.getBasicRemote().sendText(message);
//                }else {
//                    System.err.println(key + ": not open");
//                    sessionValue.close();
//                    webSocketMap.remove(key);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    @OnError
//    public void onError(Session session, Throwable error) {
//        System.out.println("Connection exception...");
//        error.printStackTrace();
//    }
//    @OnClose
//    public void onClose() {
//        System.out.println("Connection closed");
//    }
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//    public static synchronized void addOnlineCount() {
//        WebSocketServer.onlineCount++;
//    }
//
//    public static synchronized void subOnlineCount() {
//        WebSocketServer.onlineCount--;
//    }
//

@Controller
@Component
@Scope("prototype")
@ServerEndpoint("/signal/{key}")
public class WebRtcSignalingEndpoint {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    private String userId = "";
    private Session session;
    private static HashMap<Integer, ConcurrentHashMap> allChats = new HashMap<>();
    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();

    Users user1 = new Users(100, "Nodirbek");
    Users user2 = new Users(101, "Azizbek");

    @OnOpen
    public void onOpen(Session session, @PathParam("key") String key) {

        this.userId = key;
        webSocketMap.put(key, session);
        System.out.println(key + " - Connection established successfully...");

    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("key") Integer sellerId) {
        System.out.println(sellerId);
        try {
            Enumeration<String> keys =webSocketMap.keys();
            System.out.println("Messages received by the server:"+message);

            while(keys.hasMoreElements()) {
                String key = keys.nextElement();
                //Determine if the user is still online
                if (webSocketMap.get(key) == null){
                    webSocketMap.remove(key);
                    System.err.println(key + " : null");
                    continue;
                }
                Session sessionValue = webSocketMap.get(key);
                //Remove Forwarding to Yourself
                if (key.equals(this.userId)){
                    System.err.println("my id " + key);
                    continue;
                }
                //Determine if session is open
                if (sessionValue.isOpen()){
                    sessionValue.getBasicRemote().sendText(message);
                }else {
                    System.err.println(key + ": not open");
                    sessionValue.close();
                    webSocketMap.remove(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void whenClosing(Session session) {
        System.out.println("Close!"+session);
        sessions.remove(session);
    }

    @GetMapping("/api/users")
    public ResponseEntity<HashMap> getUser(){
        return ResponseEntity.ok(allChats);
    }
}
//
//package com.websocket;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Component
//public class SocketHandler extends TextWebSocketHandler {
//
//    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.add(session);
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        sessions.remove(session);
//    }
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message)
//            throws IOException {
//        for (WebSocketSession webSocketSession : sessions) {
//            if (!session.equals(webSocketSession)) {
//                webSocketSession.sendMessage(message);
//            }
//        }
//    }
//}