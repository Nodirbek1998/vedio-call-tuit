package uz.tuit.vediocall.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


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
@ServerEndpoint("/signal")
public class WebRtcSignalingEndpoint {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void whenOpening(Session session) throws IOException, EncodeException {
        System.out.println("Open!");
        // Add websocket session to a global set to use in OnMessage.
        sessions.add(session);
    }

    @OnMessage
    public void process(String data, Session session) throws IOException {
        System.out.println("Got signal - " + data);
        /*
         * When signal is received, send it to other participants other than self. In
         * real world, signal should be sent to only participant's who belong to
         * particular video conference.
         */
        for (Session sess : sessions) {
            if (!sess.equals(session)) {
                sess.getBasicRemote().sendText(data);
            }
        }
    }

    @OnClose
    public void whenClosing(Session session) {
        System.out.println("Close!");
        sessions.remove(session);
    }

}


