package com.example.acgforum.util;

import org.apache.logging.log4j.message.Message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ChatUtil {

    public static class ProtocolMessage {
        public int ver;
        public int type;
        public String satoken;
        public int receiver;
        public String payload;

        public ProtocolMessage() {
        }
    }

    public static String MessageToString(ProtocolMessage message) {
        if (message == null) {
            return "null";
        }
        // 使用 String.format 或 StringBuilder 来构建字符串
        return String.format(
                "ProtocolMessage [ver=%d, type=%d, satoken='%s', receiver=%d, payload='%s']",
                message.ver,
                message.type,
                // 对 satoken 进行 trim() 操作，去除可能存在的空格填充，使其更美观
                message.satoken != null ? message.satoken.trim() : "null",
                message.receiver,
                message.payload
        );
    }

    public static ProtocolMessage parse(byte[] data) {
        if (data.length < 1 + 1 + 36 + 4 + 4) {
            return null;
        }
        ByteBuffer bb = ByteBuffer.wrap(data);
        int ver = bb.get() & 0xFF;
        int type = bb.get() & 0xFF;
        byte[] tokenBytes = new byte[36];
        bb.get(tokenBytes);
        String satoken = new String(tokenBytes, StandardCharsets.UTF_8);
        int receiver = bb.getInt();
        int len = bb.getInt();
        if (bb.remaining() < len) {
            return null;
        }
        byte[] payloadBytes = new byte[len];
        bb.get(payloadBytes);
        String payload = new String(payloadBytes, StandardCharsets.UTF_8);

        ProtocolMessage msg = new ProtocolMessage();
        msg.ver = ver;
        msg.type = type;
        msg.satoken = satoken;
        msg.receiver = receiver;
        msg.payload = payload;
        return msg;
    }

    public static byte[] build(int ver, int type, String satoken, int receiver, String payload) {
        byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
        int len = payloadBytes.length;
        byte[] tokenBytes = satoken.getBytes(StandardCharsets.UTF_8);
        if (tokenBytes.length != 36) {
            // Pad or truncate to 36 bytes
            byte[] fixedToken = new byte[36];
            System.arraycopy(tokenBytes, 0, fixedToken, 0, Math.min(tokenBytes.length, 36));
            tokenBytes = fixedToken;
        }
        int totalLength = 1 + 1 + 36 + 4 + 4 + len;
        ByteBuffer bb = ByteBuffer.allocate(totalLength);
        bb.put((byte) ver);
        bb.put((byte) type);
        bb.put(tokenBytes);
        bb.putInt(receiver);
        bb.putInt(len);
        bb.put(payloadBytes);
        return bb.array();
    }
}