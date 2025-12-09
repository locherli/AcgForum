package com.example.acgforum.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.acgforum.entity.ChatMessage;
import com.example.acgforum.mappers.ChatMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import com.example.acgforum.util.ChatUtil;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

//协议设计

//ver（版本，1字节）
//type（操作类型，1字节）
//satoken（36字节的字符串，如dbd59e50-9d81-4414-b890-c61a2a486694）
//receiver（接收者的id，4字节，Integer类型）
//len（消息长度，4字节，Integer类型）
//payload(消息内容）


// 使用 @ServerEndpoint 注解表示此类是一个 WebSocket 端点
// 通过 value 注解，指定 websocket 的路径
@Component
@CrossOrigin(origins = "*")
@ServerEndpoint(value = "/api/chat")
public class ChatController {

    public static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    // 这里的关键是 static，让它属于类而不是对象
    private static ChatMapper chatMapper;

    // 通过 setter 方法让 Spring 在启动时给静态变量赋值
    @Autowired
    public void setChatMapper(ChatMapper chatMapper) {
        ChatController.chatMapper = chatMapper;
    }

    private static final ConcurrentHashMap<Integer, Session> onlineUsers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Session, Integer> userBySession = new ConcurrentHashMap<>();

    private static final String EMPTY_SATOKEN = new String(new char[36]).replace('\0', ' ');

    private Session session;

    // 收到消息
    @OnMessage
    public void onMessage(byte[] message, Session session) throws IOException {
        logger.info("[websocket] 收到消息：id={}, length={}", session.getId(), message.length);

        ChatUtil.ProtocolMessage msg = ChatUtil.parse(message);
        logger.info("解析后内容：{}", ChatUtil.MessageToString(msg));

        if (msg == null) {
            logger.warn("[websocket] 无效消息格式：id={}", session.getId());
            return;
        }

        if (msg.ver != 1) {
            logger.warn("[websocket] 不支持的版本：ver={}", msg.ver);
            return;
        }

        String loginIdStr = (String) StpUtil.getLoginIdByToken(msg.satoken);
        int senderId = 0;
        try {
            senderId = Integer.parseInt(loginIdStr);
        } catch (NumberFormatException e) {
            logger.warn("[websocket] 无效的 satoken 或 loginId：id={}, satoken={}", session.getId(), msg.satoken);
            return;
        }

        if (senderId <= 0) {
            logger.warn("[websocket] 无效的 satoken：id={}", session.getId());
            return;
        }

        // 关联 session 与 userId，确保发送方/连接方上线
        onlineUsers.put(senderId, session);
        userBySession.put(session, senderId);
        logger.info("[websocket] 用户已注册/更新在线状态：senderId={}", senderId);

        // type=0 为握手/注册，type=1 为发送消息
        if (msg.type == 0) {
            // type 0 仅用于注册在线状态，无需后续处理
            return;
        }

        if (msg.type == 1) {

            // 存储消息到数据库
            ChatMessage chatMessage = new ChatMessage();

            chatMessage.setSender(senderId); // 使用验证后的 senderId
            chatMessage.setReceiver(msg.receiver);
            chatMessage.setContent(msg.payload);
            chatMessage.setDate(LocalDateTime.now());
            chatMessage.setIsRead(false);
            chatMapper.insert(chatMessage);  // 假设 ChatMapper 有 insert 方法

            // 发送给接收者，如果在线
            Session receiverSession = onlineUsers.get(msg.receiver);
            if (receiverSession != null && receiverSession.isOpen()) {
                // 对于发送给客户端的消息，使用空 satoken，receiver 字段作为 senderId
                byte[] outMessage = ChatUtil.build(1, 1, EMPTY_SATOKEN, senderId, msg.payload);
                receiverSession.getBasicRemote().sendBinary(ByteBuffer.wrap(outMessage));
                logger.info("[websocket] 消息已发送给接收者：receiverId={}", msg.receiver);
            } else {
                logger.info("[websocket] 接收者不在线：receiverId={}", msg.receiver);
            }
        } else {
            logger.warn("[websocket] 不支持的操作类型：type={}", msg.type);
        }
    }

    // 连接打开
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // 保存 session 到对象
        this.session = session;
        logger.info("[websocket] 新的连接：id={}", this.session.getId());
    }

    // 连接关闭
    @OnClose
    public void onClose(CloseReason closeReason) {
        logger.info("[websocket] 连接断开：id={}，reason={}", this.session.getId(), closeReason);
        Integer userId = userBySession.remove(this.session);
        if (userId != null) {
            onlineUsers.remove(userId);
        }
    }

    // 连接异常
    @OnError
    public void onError(Throwable throwable) throws IOException {
        logger.info("[websocket] 连接异常：id={}，throwable={}", this.session.getId(), throwable.getMessage());
        // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }
}