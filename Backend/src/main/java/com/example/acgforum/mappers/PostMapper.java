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

    @Select("select * from post where authorId=#{authorId} limit #{num} offset #{offset}")
    ArrayList<Post> getByAuthorLimit(Integer authorId, Integer num, Integer offset);

    @Select("select * from post where (title like concat('%',#{keyword},'%') or content " +
            " like concat('%',#{keyword},'%')) and isRoot = 1 order by likeNum desc, date desc")
    ArrayList<Post> searchKeyword(String keyword);

    @Select("select * from post where (title like concat('%',#{keyword},'%') or content " +
            " like concat('%',#{keyword},'%')) and isRoot = 1 order by likeNum desc, date desc limit #{num} offset #{offset}")
    ArrayList<Post> searchKeywordLimit(String keyword, Integer num, Integer offset);

    @Select("select id from post where isRoot=false and root =#{id}")
    ArrayList<Integer> subPostList(Integer id);

    @Select("select * from latest_posts limit #{num} offset #{offset}")
    ArrayList<Post> latestPostList(Integer num, Integer offset);

    @Select("select * from mostLiked_posts limit #{num} offset #{offset}")
    ArrayList<Post> mostLikedPostList(Integer num, Integer offset);

    @Select("select * from mostLiked_post_week limit #{num} offset #{offset}")
    ArrayList<Post> weeklyMostLikedPostList(Integer num, Integer offset);

    @Select("select * from mostLiked_post_month limit #{num} offset #{offset}")
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

    @Select("""
                SELECT ulp_rec.post
                FROM user_likedPost ulp_rec
                WHERE ulp_rec.user IN (
                    SELECT DISTINCT ulp_other.user
                    FROM user_likedPost ulp_target
                    INNER JOIN user_likedPost ulp_other ON ulp_target.post = ulp_other.post
                    WHERE ulp_target.user = #{userId} AND ulp_other.user != #{userId}
                )
                AND ulp_rec.post NOT IN (
                    SELECT post FROM user_likedPost WHERE user = #{userId}
                )
                GROUP BY ulp_rec.post
                ORDER BY COUNT(ulp_rec.user) DESC
            """)
    ArrayList<Integer> getRecommendedPostIds(@Param("userId") int userId);

    @Select("""
                SELECT p.*
                FROM post p
                INNER JOIN (
                    SELECT ulp_rec.post, COUNT(ulp_rec.user) AS score
                    FROM user_likedPost ulp_rec
                    WHERE ulp_rec.user IN (
                        SELECT DISTINCT ulp_other.user
                        FROM user_likedPost ulp_target
                        INNER JOIN user_likedPost ulp_other ON ulp_target.post = ulp_other.post
                        WHERE ulp_target.user = #{userId} AND ulp_other.user != #{userId}
                    )
                    AND ulp_rec.post NOT IN (
                        SELECT post FROM user_likedPost WHERE user = #{userId}
                    )
                    GROUP BY ulp_rec.post
                ) AS rec ON p.id = rec.post
                ORDER BY rec.score DESC
            """)
    ArrayList<Post> getRecommendedPosts(@Param("userId") int userId);

    @Select("""
                SELECT p.*
                FROM post p
                INNER JOIN (
                    SELECT ulp_rec.post, COUNT(ulp_rec.user) AS score
                    FROM user_likedPost ulp_rec
                    WHERE ulp_rec.user IN (
                        SELECT DISTINCT ulp_other.user
                        FROM user_likedPost ulp_target
                        INNER JOIN user_likedPost ulp_other ON ulp_target.post = ulp_other.post
                        WHERE ulp_target.user = #{userId} AND ulp_other.user != #{userId}
                    )
                    AND ulp_rec.post NOT IN (
                        SELECT post FROM user_likedPost WHERE user = #{userId}
                    )
                    GROUP BY ulp_rec.post
                ) AS rec ON p.id = rec.post
                ORDER BY rec.score DESC
                LIMIT #{num} offset #{offset}
            """)
    ArrayList<Post> getRecommendedPostsLimit(@Param("userId") int userId, @Param("num") int num, @Param("offset") int offset);

    @Delete("delete from post where id = #{id}")
    Integer deletePostById(Integer id);


    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("insert into post (authorId, isRoot,root, title, date, likeNum, commentNum, content, forum)" +
            " values (#{authorId},#{isRoot},#{root},#{title},#{date},#{likeNum},#{commentNum},#{content},#{forum}) ")
    Integer insertPost(Post p);


    @Update("update post set likeNum=#{likeNum}, " +
            "title=#{title},commentNum=#{commentNum}, content=#{content} where id=#{id}")
    void update(Post p);


}
