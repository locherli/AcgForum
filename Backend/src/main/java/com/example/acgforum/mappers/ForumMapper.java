package com.example.acgforum.mappers;

import com.example.acgforum.entity.Forum;
import org.apache.ibatis.annotations.*;
import com.example.acgforum.entity.Post;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;

@Mapper
public interface ForumMapper {

    @Cacheable(value = "forum", key = "'all_forums_list'")
    @Select("select * from forum")
    ArrayList<Forum> list();

    @Cacheable(value = "forum", key = "'popular_forums_list'")
    @Select("select * from forum order by userNum")
    ArrayList<Forum> mostPopularForums();

    @Cacheable(value = "forum", key = "'latest_posts_' + #forumId")
    @Select("select * from post where forum = #{forumId} and isRoot = true order by date desc")
    ArrayList<Post> getLatestPostListByForumId(Integer forumId);

    @Select("select * from post where forum = #{forumId} and isRoot = true order by likeNum desc")
    @Cacheable(value = "forum", key = "'popular_posts_' + #forumId")
    ArrayList<Post> getMostPopularPostListByForumId(Integer forumId);

    @Select("select * from forum where name=#{name}")
    @Cacheable(value = "forum", key = "#name")
    Forum getForumByName(String name);

    @Select("select * from forum where id=#{id}")
    @Cacheable(value = "forum", key = "#id")
    Forum getForumById(Integer id);


    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("INSERT INTO forum (name, introduction, owner, postNum, userNum, icon) VALUES (#{name}, #{introduction}, #{owner}, #{postNum}, #{userNum}, #{icon})")
    @CacheEvict(value = "forum", key = "'all_forums_list'")
    void insertForum(Forum forum);

    @Update("update forum set "
            + "name = #{name}, "
            + "introduction = #{introduction}, "
            + "owner = #{owner}, "
            + "postNum = #{postNum}, "
            + "userNum = #{userNum}, "
            + "icon = #{icon} "
            + "where id = #{id}")
    @CacheEvict(value = "forum", key = "#forum.id")
    void updateForum(Forum forum);


}
