package com.example.acgforum.mappers;

import com.example.acgforum.tables.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface UserMapper {

    @Select("select count(*)>=1 from user_collection where user = #{userId} and post = #{postId}")
    Boolean isCollected(Integer userId, Integer postId);

/*    @Select("select *, avatar as avatarUrl from user_info where userName like concat('%',#{keyword},'%')")
    ArrayList<User> searchUsers(String keyword);

    @Select("select *, avatar as avatarUrl from user_info where userName like concat('%',#{keyword},'%') limit #{num} offset #{offset}")
    ArrayList<User> searchUsersLimit(String keyword, Integer num, Integer offset);*/

    @Select("SELECT ui.*, ui.avatar as avatarUrl, COUNT(rsf.id_fan) AS fanNum " +
            "FROM user_info ui " +
            "LEFT JOIN ref_self_fan rsf ON ui.id = rsf.id " +
            "WHERE ui.userName LIKE CONCAT('%', #{keyword}, '%') " +
            "GROUP BY ui.id")
    ArrayList<User> searchUsers(String keyword);

    @Select("SELECT ui.*, ui.avatar as avatarUrl, COUNT(rsf.id_fan) AS fanNum " +
            "FROM user_info ui " +
            "LEFT JOIN ref_self_fan rsf ON ui.id = rsf.id " +
            "WHERE ui.userName LIKE CONCAT('%', #{keyword}, '%') " +
            "GROUP BY ui.id " +
            "LIMIT #{num} OFFSET #{offset}")
    ArrayList<User> searchUsersLimit(String keyword, Integer num, Integer offset);

    @Select("select *,avatar as avatarUrl " +
            ",(select count(*) from ref_self_fan where id=#{id}) as fanNum " +
            ",(select count(*) from ref_self_fan where id_fan=#{id}) as subscribeNum" +
            " from user_info where id = #{id}")
    User getUserById(Integer id);

    @Select("select *,avatar as avatarUrl " +
            ",(select count(*) from ref_self_fan where id=u.id) as fanNum " +
            ",(select count(*) from ref_self_fan where id_fan=u.id) as subscribeNum" +
            " from user_info as u where email=#{email}")
    User getUserByEmail(String email);

    @Select("SELECT u.*, u.avatar as avatarUrl, " +
            "(SELECT COUNT(*) FROM ref_self_fan WHERE id = u.id) AS fanNum, " +
            "(SELECT COUNT(*) FROM ref_self_fan WHERE id_fan = u.id) AS subscribeNum " +
            "FROM user_info u " +
            "WHERE u.id IN (SELECT id FROM ref_self_fan WHERE id_fan = #{userId})")
    ArrayList<User> getSubscribedUsers(Integer userId);

    @Select("SELECT u.*, u.avatar as avatarUrl " +
            ",(select count(*) from ref_self_fan where id = u.id) as fanNum " +
            ",(select count(*) from ref_self_fan where id_fan = u.id) as subscribeNum " +
            "from user_info u where id in " +
            "(select id from ref_self_fan where id_fan = #{userId}) " +
            "limit #{num} offset #{offset}")
    ArrayList<User> getSubscribedUsersLimit(Integer userId, Integer num, Integer offset);

    @Select("select fans.id as id, userName,email,self_intro,phoneNum,hc_password,gender,age, " +
            "(select count(*) from ref_self_fan where ref_self_fan.id = fans.id) as fanNum, " +
            "(select count(*) from ref_self_fan where ref_self_fan.id_fan = fans.id) as subscribeNum " +
            "from user_info as fans " +
            "join ref_self_fan on ref_self_fan.id_fan = fans.id " +
            "where ref_self_fan.id = #{userId}")
    ArrayList<User> getFansById(Integer userId);

    @Select("select fans.id as id, userName,email,self_intro,phoneNum,hc_password,gender,age, " +
            "(select count(*) from ref_self_fan where ref_self_fan.id = fans.id) as fanNum, " +
            "(select count(*) from ref_self_fan where ref_self_fan.id_fan = fans.id) as subscribeNum " +
            "from user_info as fans " +
            "join ref_self_fan on ref_self_fan.id_fan = fans.id " +
            "where ref_self_fan.id = #{userId} limit #{num} offset #{offset}")
    ArrayList<User> getFansByIdLimit(Integer userId, Integer num, Integer offset);

    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("insert into user_info (userName, email, phoneNum, hc_password, gender, age) value" +
            "  (#{userName},#{email}, #{phoneNum}, #{hcPassword}, #{gender}, #{age})")
    void insertUser(User user);


    // avoid insert the same data.
    @Insert("INSERT IGNORE INTO user_collection (user, post) VALUES (#{userId}, #{postId})")
    void insertCollectedPost(Integer userId, Integer postId);


    @Insert("insert ignore into user_likedPost value (#{userId},#{postId})")
    void insertLikedPost(Integer userId, Integer postId);

    @Insert("insert ignore into ref_self_fan (id, id_fan) VALUE (#{id},#{fanId})")
    void insertSubscribedUser(Integer id, Integer fanId);

    @Delete("delete from ref_self_fan where id=#{myId} and id_fan=#{fanId}")
    void deleteSubscribedUser(Integer myId, Integer fanId);

    @Delete("delete from user_collection where user=#{userId} and post=#{postId}")
    void deleteCollectedPost(Integer userId, Integer postId);

    @Delete("delete from user_info where id=#{id}")
    void deleteUserById(Integer id);

    @Update("update user_info set userName=#{userName}, email=#{email}, avatar = #{avatarUrl}, self_intro = #{selfIntro}, " +
            "phoneNum=#{phoneNum}, hc_password=#{hcPassword}, gender=#{gender},age=#{age} where id=#{id}")
    void updateUser(User user);

    @Select("select count(*)=0 from user_info where email = #{email}")
    Boolean isLegalEmail(String email);

    @Select("select count(*)>=1 from user_info where email=#{email} and hc_password=#{hc_password}")
    Boolean loginByEmail(String email, Integer hc_password);

    @Select("select count(*)>=1 from user_info where userName=#{userName} and hc_password=#{hc_password}")
    Boolean loginByUserName(String userName, Integer hc_password);

    @Select("Select count(*)>=1 from user_likedPost where user=#{userId} and post=#{postId}")
    Boolean isLiked(Integer userId, Integer postId);

    @Select("select authorId from post where id = #{id}")
    Integer getAuthorIdByPostId(Integer id);


}
