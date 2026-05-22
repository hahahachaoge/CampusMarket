package com.campus.websocket;

import com.campus.dto.WebSocketMessageDTO;
import com.campus.entity.ChatMessage;
import com.campus.mapper.ChatMessageMapper;
import com.campus.utils.JwtUtils;
import com.campus.vo.MessageVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    /**
     * 在线用户
     *
     * key:
     * 用户ID
     *
     * value:
     * websocket连接
     */
    private static final Map<Long, WebSocketSession> ONLINE_USERS = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatMessageMapper chatMessageMapper;
    //建立连接
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        try{
            //获取query参数
            String query = session.getUri().getQuery();
            //判空
            if(query == null || query.isEmpty()){
                System.out.println("query为空");
                session.close();
                return;
            }
            //token=xxx
            String token = query.replace("token=","");
            System.out.println("token="+token);
            //解析token
            Long userId = JwtUtils.parseToken(token);
            //保存在线用户
            ONLINE_USERS.put(userId,session);
            System.out.println("用户上线："+ userId);
        }
        catch(Exception e){
            e.printStackTrace();
            session.close();
        }
    }

    //收到消息
    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message
    ) throws Exception{
        //收到JSON
        String payload = message.getPayload();
        System.out.println("收到消息" + payload);
        //转对象
        WebSocketMessageDTO dto = objectMapper.readValue(payload,WebSocketMessageDTO.class);
        //获取当前用户
        String query = session.getUri().getQuery();
        String token = query.replace("token=","");
        Long fromUserId = JwtUtils.parseToken(token);
        //保存聊天记录
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFromUserId(fromUserId);
        chatMessage.setToUserId(dto.getToUserId());
        chatMessage.setContent(dto.getContent());
        chatMessage.setIsRead(0);
        chatMessageMapper.insert(chatMessage);
        //找接收人
        WebSocketSession toSession = ONLINE_USERS.get(dto.getToUserId());
        //对方在线
        if(toSession != null && toSession.isOpen()){
            //推送信息
            toSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(new MessageVO(fromUserId,dto.getContent()))));
        }
    }

    //连接关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        ONLINE_USERS.values().remove(session);
        System.out.println("用户离线");
    }
}
