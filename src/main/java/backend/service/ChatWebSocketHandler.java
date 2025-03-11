package backend.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 存儲 WebSocket 連線
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session); // 新用戶加入
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        try {
            // 解析 JSON 格式 {"username": "Alan", "message": "Hello!"}
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> msgMap = objectMapper.readValue(payload, new TypeReference<Map<String, String>>() {});
            String username = msgMap.get("username");
            String msg = msgMap.get("message");

            // 廣播到 Redis
            String formattedMessage = objectMapper.writeValueAsString(Map.of("username", username, "message", msg));
            redisTemplate.convertAndSend("chat", formattedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session); // 移除關閉的連線
    }

    // 發送訊息給所有用戶
    public void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

