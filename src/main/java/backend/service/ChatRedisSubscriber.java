package backend.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ChatRedisSubscriber implements MessageListener {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler; // WebSocket 處理器

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        chatWebSocketHandler.broadcast(msg); // 轉發 JSON 格式的訊息
    }
}

