package com.example.acgforum.mappers;

import com.example.acgforum.entity.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMapper {

    /**
     * 插入聊天消息
     * 使用 @Options 获取自增 ID
     */
    @Insert("INSERT INTO chat_message(sender, receiver, content, date, is_read) " +
            "VALUES(#{sender}, #{receiver}, #{content}, #{date}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChatMessage chatMessage);

    /**
     * 查询两个用户之间的聊天记录
     * 逻辑：查询 (A发给B) 或 (B发给A) 的所有消息，按时间排序
     * 这里的 SQL 进行了联表查询 user_info，以便直接返回发送者的头像和名字
     */
    @Select("SELECT m.*, u.userName as senderName, u.avatar as senderAvatar " +
            "FROM chat_message m " +
            "LEFT JOIN user_info u ON m.sender = u.id " +
            "WHERE (m.sender = #{userId1} AND m.receiver = #{userId2}) " +
            "   OR (m.sender = #{userId2} AND m.receiver = #{userId1}) " +
            "ORDER BY m.date ASC")
    @Results({
            @Result(property = "isRead", column = "is_read")
    })
    List<ChatMessage> findHistory(@Param("userId1") Integer userId1,
                                  @Param("userId2") Integer userId2);

    /**
     * 将指定会话的消息标记为已读
     * 场景：UserId 点开了 ContactId 的对话框，那么 ContactId 发给 UserId 的未读消息都应该变更为已读
     */
    @Update("UPDATE chat_message SET is_read = 1 " +
            "WHERE sender = #{contactId} AND receiver = #{userId} AND is_read = 0")
    void markAsRead(@Param("userId") Integer userId,
                    @Param("contactId") Integer contactId);

    /**
     * 获取用户的未读消息总数
     */
    @Select("SELECT COUNT(*) FROM chat_message WHERE receiver = #{userId} AND is_read = 0")
    int countUnread(@Param("userId") Integer userId);

    /**
     * 获取最近联系人列表（视图逻辑简化版）
     * 获取当前用户参与过对话的所有用户ID，用于构建左侧聊天列表
     */
    @Select("SELECT DISTINCT " +
            "CASE WHEN sender = #{userId} THEN receiver ELSE sender END as contactId " +
            "FROM chat_message " +
            "WHERE sender = #{userId} OR receiver = #{userId}")
    List<Integer> findRecentContactIds(@Param("userId") Integer userId);
}