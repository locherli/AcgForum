package com.example.acgforum.mappers;

import com.example.acgforum.tables.Post;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface PostMapper {

    @Select("select * from post where id=#{id}")
    Post getById(Integer id);

    @Select("select * from post where authorId=#{authorId}")
    ArrayList<Post> getByAuthor(Integer authorId);

    @Select("select * from post where (title like concat('%',#{keyword},'%') or content " +
            " like concat('%',#{keyword},'%')) and isRoot = 1 order by likeNum desc, date desc")
    ArrayList<Post> searchKeyword(String keyword);

    @Select("select id from post where isRoot=false and root =#{id}")
    ArrayList<Integer> subPostList(Integer id);

    @Select("select * from latest_posts limit #{num} offset #{offset}")
    ArrayList<Post> latestPostList(Integer num, Integer offset);

    @Select("select * from mostLiked_posts limit #{num} offset #{offset}")
    ArrayList<Post> mostLikedPostList(Integer num, Integer offset);

    @Select("select * from mostLiked_post_week  limit #{num} offset #{offset}")
    ArrayList<Post> weeklyMostLikedPostList(Integer num, Integer offset);

    @Select("select * from mostLiked_post_month  limit #{num} offset #{offset}")
    ArrayList<Post> monthlyMostLikedPostList(Integer num, Integer offset);

    @Select("select * from post join user_collection on user_collection.post = post.id" +
            " where user_collection.user=#{userId}")
    ArrayList<Post> collectedPostListByUserId(Integer userId);


    @Select("select * from post join user_collection on user_collection.post = post.id" +
            " where user_collection.user=#{userId} limit #{num} offset #{offset}")
    ArrayList<Post> collectedPostListByUserIdLimit(Integer userId, Integer num, Integer offset);

    @Select("select * from user_likedPost join post on post.id = user_likedPost.post " +
            "where user_likedPost.user = #{userId} ")
    ArrayList<Post> likedPostListByUserId(Integer userId);

    @Delete("delete from post where id = #{id}")
    Integer deletePostById(Integer id);


    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("insert into post (authorId, isRoot,root, title, date, likeNum, commentNum, content)" +
            "        values (#{authorId},#{isRoot},#{root},#{title},#{date},#{likeNum},#{commentNum},#{content}) ")
    Integer insertPost(Post p);


    @Update("update post set likeNum=#{likeNum}, " +
            "title=#{title},commentNum=#{commentNum}, content=#{content} where id=#{id}")
    void update(Post p);




}
