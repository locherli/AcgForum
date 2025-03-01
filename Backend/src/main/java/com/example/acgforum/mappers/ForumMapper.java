package com.example.acgforum.mappers;

import com.example.acgforum.tables.Forum;
import org.apache.ibatis.annotations.*;
import com.example.acgforum.tables.Post;
import java.util.ArrayList;

@Mapper
public interface ForumMapper {

    @Select("select * from forum")
    ArrayList<Forum> list();

    @Select("select * from forum order by userNum")
    ArrayList<Forum> mostPopularForums();

    @Select("select * from post where forum = #{forumId} and isRoot = true order by date desc")
    ArrayList<Post> getLatestPostListByForumId(Integer forumId);

    @Select("select * from post where forum = #{forumId} and isRoot = true order by likeNum desc")
    ArrayList<Post> getMostPopularPostListByForumId(Integer forumId);

    @Select("select * from forum where name=#{name}")
    Forum getForumByName(String name);

    @Select("select * from forum where id=#{id}")
    Forum getForumById(Integer id);

    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("INSERT INTO forum (name, introduction, owner, postNum, userNum, icon) VALUES (#{name}, #{introduction}, #{owner}, #{postNum}, #{userNum}, #{icon})")
    void insertForum(Forum forum);

    @Update("update forum set "
            + "name = #{name}, "
            + "introduction = #{introduction}, "
            + "owner = #{owner}, "
            + "postNum = #{postNum}, "
            + "userNum = #{userNum}, "
            + "icon = #{icon} "
            + "where id = #{id}")
    void updateForum(Forum forum);


}
