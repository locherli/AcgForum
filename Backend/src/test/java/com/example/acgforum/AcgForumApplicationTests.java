package com.example.acgforum;

import com.example.acgforum.ReturnFormat.ReturnUser;
import com.example.acgforum.mappers.PostMapper;
import com.example.acgforum.mappers.UserMapper;
import com.example.acgforum.tables.Post;
import com.example.acgforum.tables.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.acgforum.RequestController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@SpringBootTest
class AcgForumApplicationTests {
    @Resource(name = "postMapper")
    PostMapper postMapper;
    @Resource(name = "userMapper")
    UserMapper userMapper;
    @Test
    void contextLoads() {
    }

}
