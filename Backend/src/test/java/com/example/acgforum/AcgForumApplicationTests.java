package com.example.acgforum;

import com.example.acgforum.mappers.ForumMapper;
import com.example.acgforum.mappers.PostMapper;
import com.example.acgforum.mappers.UserMapper;
import com.example.acgforum.tables.Post;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class AcgForumApplicationTests {
    @Resource(name = "postMapper")
    PostMapper postMapper;
    @Resource(name = "userMapper")
    UserMapper userMapper;
    @Resource(name="forumMapper")
    ForumMapper forumMapper;

    @Test
    void contextLoads() {

        System.out.println(userMapper.searchUsers("lo"));

    }

}
