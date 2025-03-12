package backend.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.List;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;


    // 存儲 WebSocket 連線
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String token = extractToken(session);
    
        if (token == null) {
            closeSession(session, new CloseStatus(1006, "請登入後連線至聊天室"));
            return;
        }
    
        String username = jwtUtil.extractUsername(token);
        if (!jwtUtil.validateToken(token, username)) {
            closeSession(session, new CloseStatus(1006, "權限驗證失敗，請重新登入"));
            return;
        }
    
        List<String> roles = jwtUtil.extractRoles(token);
        System.out.println(username + " " + roles);
    
        // 只有 admin 才能連線
        if (!roles.contains("admin")) {
            closeSession(session, new CloseStatus(1008, "無使用聊天室的權限，請先升級"));
            return;
        }
    
        // 綁定使用者名稱到 WebSocket Session
        session.getAttributes().put("username", username);
        sessions.add(session);
    }

    private String extractToken(WebSocketSession session) {
        // 1️⃣ 嘗試從 Header 取得 Authorization Token
        List<String> authHeaders = session.getHandshakeHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String bearerToken = authHeaders.get(0);
            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }
    
        // 2️⃣ 嘗試從 URL 參數取得 Token
        String query = session.getUri().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("token=")) {
                    return param.substring(6);
                }
            }
        }
    
        return null; // 沒有找到 Token
    }

    private void closeSession(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

