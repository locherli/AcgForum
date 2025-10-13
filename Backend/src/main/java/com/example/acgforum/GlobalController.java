package com.example.acgforum;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import com.example.acgforum.ReturnFormat.*;
import com.example.acgforum.mappers.ForumMapper;
import com.example.acgforum.mappers.PostMapper;
import com.example.acgforum.mappers.UserMapper;
import com.example.acgforum.tables.Forum;
import com.example.acgforum.tables.Post;
import com.example.acgforum.tables.User;
import com.github.pagehelper.PageHelper;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class GlobalController {
    Logger logger = LoggerFactory.getLogger("CONTROLLER'S LOGGER");
    @Value("${file.path}")
    private String filePath;
    @Resource(name = "postMapper")
    PostMapper postMapper;
    @Resource(name = "userMapper")
    UserMapper userMapper;
    @Resource(name = "forumMapper")
    ForumMapper forumMapper;

    // 辅助方法：验证必需字段
    private Map<String, Object> validateRequiredFields(Map<String, String> requestBody, String... requiredFields) {
        Map<String, Object> errorResponse = new HashMap<>();

        for (String field : requiredFields) {
            if (!requestBody.containsKey(field)) {
                errorResponse.put("success", false);
                errorResponse.put("message", field + " 不能为空");
                return errorResponse;
            }

            if (requestBody.get(field) == null || requestBody.get(field).trim().isEmpty()) {
                errorResponse.put("success", false);
                errorResponse.put("message", field + " 不能为空");
                return errorResponse;
            }
        }

        return null; // 表示验证通过
    }

    // 辅助方法：计算帖子中关键字出现的次数
    public int countKeywordOccurrences(String content, String keyword) {
        String lowerContent = content.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();
        int count = 0;
        int index = 0;
        while ((index = lowerContent.indexOf(lowerKeyword, index)) != -1) {
            count++;
            index += lowerKeyword.length();
        }
        return count;
    }

    //辅助方法
    private ArrayList<ReturnSimplePost> getReturnSimplePosts(ArrayList<Post> pl) {
        ArrayList<ReturnSimplePost> rl = new ArrayList<>();
        for (var i : pl) {
            //make sure the response body won't too long.
            if (i.getTitle() == null) {
                if (i.getContent().length() > 50)
                    i.setTitle(i.getContent().substring(0, 50) + "...");
                else
                    i.setTitle(i.getContent());
            }

            String authorName = userMapper.getUserById(i.getAuthorId()).getUserName();
            String avatarUrl = userMapper.getUserById(i.getAuthorId()).getAvatarUrl();

            rl.add(new ReturnSimplePost(i.getId(),
                    i.getTitle(), i.getDate(), i.getLikeNum(), i.getCommentNum(), authorName, avatarUrl));
        }

        return rl;
    }

    @GetMapping("/")
    public RedirectView test() {
        // String testUrl = "hello";
        String testUrl = "http://localhost/index.html";
        return new RedirectView(testUrl);
    }


    @GetMapping("/post")
    public ReturnPost post(@RequestParam Integer id) {
        Post p = postMapper.getById(id);
        User author = userMapper.getUserById(p.getAuthorId());
        ArrayList<Integer> arr = postMapper.subPostList(id);

        return new ReturnPost(p.getId(), "success", p.getAuthorId(), (author == null) ? "用户已注销" : author.getUserName(),
                p.getTitle(), p.getDate(), p.getContent(), p.getLikeNum(), p.getCommentNum(), arr);
    }

    @GetMapping("/most_liked_post")
    public ArrayList<ReturnSimplePost> mostLikedPost(@RequestParam Integer page, @RequestParam String interval) {
        ArrayList<Post> postList;

        if (interval.equalsIgnoreCase("week")) {
            postList = postMapper.weeklyMostLikedPostList(30, (page - 1) * 30);
        } else if (interval.equalsIgnoreCase("month")) {
            postList = postMapper.monthlyMostLikedPostList(30, (page - 1) * 30);
        } else {
            postList = postMapper.mostLikedPostList(30, (page - 1) * 30);
        }

        return getReturnSimplePosts(postList);
    }

    @GetMapping("/latest_post")
    public ArrayList<ReturnSimplePost> latestPost(@RequestParam Integer page) {
        return getReturnSimplePosts(postMapper.latestPostList(30, (page - 1) * 30));
    }

    @GetMapping("recommend_post")
    public ArrayList<ReturnSimplePost> recommendPost(@RequestParam Integer page) {
        int user_id = -1;
        try {
            user_id = StpUtil.getLoginIdAsInt();
        } catch (NotLoginException e) {
            // 处理未登录情况
            return new ArrayList<ReturnSimplePost>();
        }

        ArrayList<Post> recommendedPosts = postMapper.getRecommendedPostsLimit(user_id, 30, (page - 1) * 30);


        return getReturnSimplePosts(recommendedPosts);
    }

    @GetMapping("/user")
    public ReturnUser user(@RequestParam Integer id) {

        User u = userMapper.getUserById(id);
        if (u == null)
            return new ReturnUser();

        if (u.getAvatarUrl() == null)
            u.setAvatarUrl("default-avatar.png");
        ReturnUser returnUser = new ReturnUser(id, u.getUserName(), u.getAvatarUrl(),
                u.getSelfIntro(), u.getFanNum(), u.getSubscribeNum(), u.getPhoneNum(), u.getAge());


        return returnUser;
    }

    @GetMapping("/user/myself")
    public ReturnUser user_2() {

        if (!StpUtil.isLogin()) {
            return new ReturnUser();
        }

        int id = StpUtil.getLoginIdAsInt();
        User u = userMapper.getUserById(id);
        if (u == null)
            return new ReturnUser();

        if (u.getAvatarUrl() == null)
            u.setAvatarUrl("default-avatar.png");
        ReturnUser returnUser = new ReturnUser(id, u.getUserName(), u.getAvatarUrl(),
                u.getSelfIntro(), u.getFanNum(), u.getSubscribeNum(), u.getPhoneNum(), u.getAge());


        return returnUser;
    }

    @GetMapping("/collected_posts")
    public ArrayList<ReturnSimplePost> collectedPosts(@RequestParam Integer page, @RequestParam Integer userId) {
        ArrayList<Post> postList = postMapper.collectedPostListByUserIdLimit(userId, 30, (page - 1) * 30);
        return getReturnSimplePosts(postList);
    }

    @GetMapping("/my_collected_posts")
    public ArrayList<ReturnSimplePost> myCollectedPosts(@RequestParam Integer page) {
        ArrayList<Post> postList = postMapper.collectedPostListByUserIdLimit(StpUtil.getLoginIdAsInt(), 30, (page - 1) * 30);
        return getReturnSimplePosts(postList);
    }

    @GetMapping("/fans")
    public ArrayList<User> fansById(@RequestParam Integer myId) {
        return userMapper.getFansById(myId);
    }

    @GetMapping("/my_fans")
    public ArrayList<User> myFans(@RequestParam Integer page) {
        return userMapper.getFansByIdLimit(StpUtil.getLoginIdAsInt(), 30, (page - 1) * 30);
    }

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody Map<String, String> requestBody) {
        System.out.println(requestBody.toString());

        Map<String, Object> returnObj = new HashMap<>();

        // 验证必需字段
        Map<String, Object> validationError = validateRequiredFields(requestBody, "email", "userName", "password");
        if (validationError != null) {
            return validationError;
        }

        boolean isLegalEmail = userMapper.isLegalEmail(requestBody.get("email"));

        // store user into database.
        if (isLegalEmail) {
            User newUser = new User();
            newUser.setUserName(requestBody.get("userName"));
            newUser.setEmail(requestBody.get("email"));
            newUser.setHcPassword(requestBody.get("password").hashCode());

            userMapper.insertUser(newUser);

            returnObj.put("success", true);
            returnObj.put("message", "注册成功");
        } else {
            returnObj.put("success", false);
            returnObj.put("message", "邮箱已被使用");
        }

        // Return message.
        return returnObj;
    }
/*
    @PostMapping("/put_post")
    public Map<String, Object> putPost(@RequestBody Map<String, String> requestBody) {
        try {

            if(!StpUtil.isLogin()){
                return Map.of("msg","unauthorized");
            }

            // 验证必需字段
            Map<String, Object> validationError = validateRequiredFields(requestBody, "isRoot", "content", "forumId");
            if (validationError != null) {
                return validationError;
            }

            int userId = StpUtil.getLoginIdAsInt();

            // 对于非根帖子，还需要验证 root 字段
            boolean isRoot = requestBody.get("isRoot").equalsIgnoreCase("true");
            if (!isRoot) {
                Map<String, Object> rootValidationError = validateRequiredFields(requestBody, "root");
                if (rootValidationError != null) {
                    return rootValidationError;
                }
            } else {
                Map<String, Object> rootPostValidationError = validateRequiredFields(requestBody, "title");
                if (rootPostValidationError != null) {
                    return rootPostValidationError;
                }
            }

            Integer root = null; // Declare the variable here

            if (!isRoot) *//*if this post was a comment.*//* {
                //comments number + 1
                root = Integer.parseInt(requestBody.get("root"));
                Post p = postMapper.getById(root);
                p.setCommentNum(p.getCommentNum() + 1);
                postMapper.update(p);
            }
            Post p = new Post(null, userId, isRoot, root
                    , requestBody.get("title"), LocalDateTime.now(), 0, 0
                    , requestBody.get("content"), Integer.parseInt(requestBody.get("forumId")));

            postMapper.insertPost(p);

            return Map.of("success", true);

        } catch (NumberFormatException | NullPointerException e) {
            // Handle the case where parsing to integer fails or requestBody is null
            System.out.println("Invalid user ID format or null request body: " + e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }*/

    @PostMapping("/put_post")
    public Map<String, Object> putPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("forumId") String forumId) {
        try {
            if (!StpUtil.isLogin()) {
                return Map.of("msg", "unauthorized");
            }

            // 验证必需字段
            if (title == null || title.isEmpty() || content == null || content.isEmpty() || forumId == null || forumId.isEmpty()) {
                return Map.of("success", false, "message", "Missing required fields: title, content, forumId");
            }

            int userId = StpUtil.getLoginIdAsInt();

            Post p = new Post(null, userId, true, null, title, LocalDateTime.now(), 0, 0, content, Integer.parseInt(forumId));

            postMapper.insertPost(p);

            return Map.of("success", true);

        } catch (NumberFormatException e) {
            System.out.println("Invalid format: " + e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @PostMapping("/put_comment")
    public Map<String, Object> putComment(
            @RequestParam("root") String rootStr,
            @RequestParam("content") String content) {
        try {
            if (!StpUtil.isLogin()) {
                return Map.of("msg", "unauthorized");
            }

            // 验证必需字段
            if (rootStr == null || rootStr.isEmpty() || content == null || content.isEmpty()) {
                return Map.of("success", false, "message", "Missing required fields: root, content, forumId");
            }

            int userId = StpUtil.getLoginIdAsInt();
            int root = Integer.parseInt(rootStr);

            // Increment comments number for the root post
            Post rootPost = postMapper.getById(root);
            if (rootPost == null) {
                return Map.of("success", false, "message", "Root post not found");
            }
            rootPost.setCommentNum(rootPost.getCommentNum() + 1);
            postMapper.update(rootPost);

            Post p = new Post(null, userId, false, root, null, LocalDateTime.now(), 0, 0, content, null);

            postMapper.insertPost(p);

            return Map.of("success", true);

        } catch (NumberFormatException e) {
            System.out.println("Invalid format: " + e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @PostMapping("/login")
    public SaResult login(@RequestParam("email") String email,
                          @RequestParam("password") String password) {
        // 验证必需字段
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return SaResult.error("参数错误");
        }

        Integer hc_password = password.hashCode();

        Boolean token = userMapper.loginByEmail(email, hc_password);

        if (!token) {
            return SaResult.error("邮箱或密码错误");
        } else {
            User user = userMapper.getUserByEmail(email);
            StpUtil.login(user.getId());
            ReturnUser returnUser = new ReturnUser(user.getId(), user.getUserName(), user.getAvatarUrl(), user.getSelfIntro(),
                    user.getFanNum(), user.getSubscribeNum(), user.getPhoneNum(), user.getAge());
            return SaResult.ok("登录成功").setData(user);
        }
    }

    @GetMapping("/is_login")
    public SaResult isLogin() {
        return SaResult.ok("" + StpUtil.isLogin());
    }

    @RequestMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    @PostMapping("/delete_post")
    public Map<String, Object> deletePost(@RequestParam Integer id) {
        Post post = postMapper.getById(id);
        if (post == null)
            return Map.of("success", false);

        if (post.getIsRoot()) {
            ArrayList<Integer> subPostList = postMapper.subPostList(id);
            subPostList.forEach(i -> {
                postMapper.deletePostById(i);
            });
        }

        postMapper.deletePostById(id);
        return Map.of("success", true);
    }

    @PostMapping("/delete_user")
    public Map<String, Object> deleteUser(@RequestParam Integer id) {
        userMapper.deleteUserById(id);
        return Map.of("success", true);
    }

    @PostMapping("/like_post")
    public Map<String, Boolean> likePost(@RequestParam Integer postId) {

        Integer userId = -1;
        try {
            userId = StpUtil.getLoginIdAsInt();
        } catch (NotLoginException e) {
            logger.info("用户未登录：{}", e.toString());
            return Map.of("isOk", false);
        }

        //if this user already liked this post.
        if (userMapper.isLiked(userId, postId))
            return Map.of("isOk", false);

        //likeNum++
        Post originPost = postMapper.getById(postId);
        originPost.setLikeNum(originPost.getLikeNum() + 1);
        postMapper.update(originPost);
        //insert this relationship.
        userMapper.insertLikedPost(userId, postId);

        return Map.of("isOk", true);
    }

    @PostMapping("/collect_post")
    public Map<String, Boolean> collectPost(@RequestParam Integer postId) {

        Integer userId = -1;
        try {
            userId = StpUtil.getLoginIdAsInt();
        } catch (NotLoginException e) {
            logger.info("用户未登录：{}", e.toString());
            return Map.of("isOk", false);
        }

        if (userMapper.isCollected(userId, postId)) {
            return Map.of("isOk", false);
        }

        userMapper.insertCollectedPost(userId, postId);
        return Map.of("isOk", true);
    }

    @PostMapping("/subscribe_user")
    public Map<String, Object> subscribeUser(@RequestParam Integer userId) {

        Integer myId = -1;
        try {
            myId = StpUtil.getLoginIdAsInt();
        } catch (NotLoginException e) {
            logger.info("用户未登录：{}", e.toString());
            return Map.of("isOk", false);
        }

        userMapper.insertSubscribedUser(userId, myId);
        return Map.of("success", true);
    }

    @GetMapping("/subposts")
    public ArrayList<ReturnComment> subPosts(@RequestParam Integer id, @RequestParam Integer page) {

        ArrayList<Integer> idxArrayList = postMapper.subPostList(id);
        ArrayList<Post> posts = new ArrayList<>();
        for (int i = (page - 1) * 30; i < idxArrayList.size() && i <= page * 30; i++) {
            int idx = idxArrayList.get(i);
            posts.add(postMapper.getById(idx));
        }

        ArrayList<ReturnComment> commentList = new ArrayList<>();
        posts.forEach(i -> {
            commentList.add(new ReturnComment(
                    i.getId(), i.getAuthorId(), i.getContent(), i.getDate(), i.getLikeNum(), i.getCommentNum()));
        });

        return commentList;
    }

    @GetMapping("/subscribed_users")
    public ArrayList<User> subscribedUsers(@RequestParam Integer id, @RequestParam Integer page) {
        ArrayList<User> subscribeUsers = userMapper.getSubscribedUsersLimit(id, 30, 30 * (page - 1));
        return subscribeUsers;
    }

    @GetMapping("/my_subscribed_users")
    public ArrayList<User> subscribedUsers(@RequestParam Integer page) {
        ArrayList<User> subscribeUsers = userMapper.getSubscribedUsersLimit(StpUtil.getLoginIdAsInt(), 30, 30 * (page - 1));
        return subscribeUsers;
    }


    @GetMapping("/posts_by_author")
    public ArrayList<ReturnSimplePost> getPostsByAuthor(@RequestParam Integer authorId) {
        ArrayList<Post> postList = postMapper.getByAuthor(authorId);
        return getReturnSimplePosts(postList);
    }

    @GetMapping("/my_posts")
    public ArrayList<ReturnSimplePost> getMyPosts(@RequestParam Integer page) {

        int myId = StpUtil.getLoginIdAsInt();
        ArrayList<Post> postList = postMapper.getByAuthorLimit(myId, 16, (page - 1) * 16);
        return getReturnSimplePosts(postList);
    }


    @GetMapping("/search_post")
    public ArrayList<ReturnSimplePost> searchPost(@RequestParam String keyword, @RequestParam Integer page, @RequestParam Integer size) {
        ArrayList<Post> pl = postMapper.searchKeywordLimit(keyword, size, (page - 1) * size);

        pl.sort((a, b) -> {
            int count_a = countKeywordOccurrences(a.getTitle() + a.getContent(), keyword);
            int count_b = countKeywordOccurrences(b.getTitle() + b.getContent(), keyword);
            return count_b - count_a;
        });

        // cast "Post" into "ReturnSimplePost".
        return getReturnSimplePosts(pl);
    }


    @GetMapping("/search_user")
    ArrayList<User> searchUser(@RequestParam String keyword, @RequestParam Integer page, @RequestParam Integer size) {
        return userMapper.searchUsersLimit(keyword, size, (page - 1) * size);
    }

    @PostMapping("/unsubscribe_user")
    public Map<String, Object> unsubscribe_user(@RequestParam Integer myId, @RequestParam Integer userId) {
        userMapper.deleteSubscribedUser(userId, myId);
        return Map.of("success", true);
    }

    @PostMapping("/uncollect_post")
    public Map<String, Object> uncollect_post(@RequestParam Integer userId, @RequestParam Integer postId) {
        userMapper.deleteCollectedPost(userId, postId);
        return Map.of("success", true);
    }

    @GetMapping("/most_popular_forums")
    public ArrayList<ReturnForum> forums(@RequestParam Integer page) {

        PageHelper.startPage(page, 6);
        ArrayList<Forum> forums = forumMapper.list();
        ArrayList<ReturnForum> returnList = new ArrayList<>();
        forums.forEach(i -> {
            ReturnForum f = new ReturnForum();
            f.setForumId(i.getId());
            f.setForumName(i.getName());
            f.setIntroduction(i.getIntroduction());
            f.setUserNum(i.getUserNum());
            f.setIcon(i.getIcon());
            returnList.add(f);
        });

        returnList.sort((a, b) -> b.getUserNum() - a.getUserNum());

        return returnList;
    }

    @GetMapping("/posts_by_forum")
    public ArrayList<ReturnSimplePost> postsByForum(@RequestParam Integer forumId, @RequestParam Integer page) {
        PageHelper.startPage(page, 30);
        ArrayList<Post> posts = forumMapper.getLatestPostListByForumId(forumId);
        return getReturnSimplePosts(posts);
    }

    @PostMapping("/create_forum")
    public Map<String, Object> createForum(@RequestBody Map<String, String> requestBody) {
        // 验证必需字段
        Map<String, Object> validationError = validateRequiredFields(requestBody, "forumName", "owner");
        if (validationError != null) {
            return validationError;
        }

        Forum queryForum = forumMapper.getForumByName(requestBody.get("forumName"));
        Map<String, Object> rm = new HashMap<>();

        if (queryForum == null) {
            Forum f = new Forum();
            f.setName(requestBody.get("forumName"));
            f.setIntroduction(requestBody.get("introduction") != null ? requestBody.get("introduction") : "");
            f.setOwner(Integer.parseInt(requestBody.get("owner")));
            f.setPostNum(0);
            f.setUserNum(0);
            f.setIcon("defaultIcon.jpg");
            forumMapper.insertForum(f);

            rm.put("isOk", true);
            rm.put("forumId", f.getId());
            rm.put("msg", "create forum success.");
        } else {
            rm.put("isOk", false);
            rm.put("forumId", null);
            rm.put("msg", "this forum was already been used.");
        }
        return rm;
    }

    @PostMapping("/change_avatar")
    Map<String, Object> changeAvatar(@RequestParam("avatarFile") MultipartFile avatarFile) {

        Map<String, Object> rm = new HashMap<>();
        int userId = -1;

        try {
            userId = StpUtil.getLoginIdAsInt();
        } catch (Exception e) {
            logger.info("更新头像时未登录：" + e.toString());
            rm.put("isOk", false);
            rm.put("message", "Unauthorized");
            return rm;
        }

        if (avatarFile == null || avatarFile.isEmpty()) {
            rm.put("isOk", false);
            rm.put("message", "头像文件不能为空");
            return rm;
        }

        try /* store the file */ {
            int lastDot = avatarFile.getOriginalFilename().lastIndexOf('.');
            String extname = avatarFile.getOriginalFilename().substring(lastDot);

            String fileName = UUID.randomUUID() + "_" + userId + extname;

            // Create the uploads directory if it doesn't exist
            File uploadDir = new File(filePath);
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    throw new IOException("Failed to create uploads directory");
                }
            }

            File fLocation = new File(uploadDir, fileName);  // Use uploadDir to build the full path

            avatarFile.transferTo(fLocation);  // No need for createNewFile(); transferTo handles creation
            logger.info("FILE PATH: " + fLocation.getAbsolutePath());

            User u = userMapper.getUserById(userId);
            u.setAvatarUrl(fileName);
            userMapper.updateUser(u);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);  // Log full stack trace for better debugging
            rm.put("isOk", false);
            rm.put("message", "Failed to upload avatar: " + e.getMessage());
            return rm;
        }

        rm.put("isOk", true);
        return rm;
    }

    @PostMapping("/change_icon")
    Map<String, Object> changeIcon(@RequestParam Integer forumId, @RequestParam("iconFile") MultipartFile iconFile) {
        //authenticate user
        if (forumMapper.getForumById(forumId) == null) {
            Map<String, Object> rm = new HashMap<>();
            rm.put("isOk", false);
            rm.put("message", "forum don't exist.");
            return rm;
        }

        if (iconFile == null || iconFile.isEmpty()) {
            Map<String, Object> rm = new HashMap<>();
            rm.put("isOk", false);
            rm.put("message", "图标文件不能为空");
            return rm;
        }

        try /*store the file into nginx server.*/ {
            int lastDot = iconFile.getOriginalFilename().lastIndexOf('.');
            String extname = iconFile.getOriginalFilename().substring(lastDot);

            String fileName = UUID.randomUUID() + extname;
            File fLocation = new File(filePath + fileName);
            logger.info("FILE PATH: " + fLocation.getAbsolutePath());

            if (!fLocation.exists())
                fLocation.createNewFile();

            iconFile.transferTo(fLocation);

            Forum f = forumMapper.getForumById(forumId);
            f.setIcon(fileName);
            forumMapper.updateForum(f);

        } catch (Exception e) {
            logger.error(e.getMessage());
            Map<String, Object> rm = new HashMap<>();
            rm.put("isOk", false);
            rm.put("message", "Failed to upload icon");
            return rm;
        }

        Map<String, Object> rm = new HashMap<>();
        rm.put("isOk", true);
        return rm;
    }

    @GetMapping("/forum")
    Forum getForumById(@RequestParam Integer id) {
        return forumMapper.getForumById(id);
    }


    @PostMapping("/alter_user_info")
    Map<String, Object> alterUserInfo(@RequestParam String userName,
                                      @RequestParam String selfIntro,
                                      @RequestParam String phoneNum,
                                      @RequestParam boolean gender,
                                      @RequestParam Integer age) {

        if (!StpUtil.isLogin()) {
            logger.info("用户未登录");
            return Map.of("success", false, "message", "用户未登录");
        }

        try {
            int userId = StpUtil.getLoginIdAsInt();
            User u = userMapper.getUserById(userId);

            logger.error(u.toString());

            u.setUserName(userName);
            u.setSelfIntro(selfIntro);
            u.setPhoneNum(phoneNum);
            u.setGender(gender);
            u.setAge(age);

            userMapper.updateUser(u);

            return Map.of("success", true, "message", "成功更改信息");
        } catch (Exception e) {
            logger.error("更改用户信息时出错：{}", e.toString());
            return Map.of("success", false, "message", "后端异常");
        }
    }


    @PostMapping("/alter_forum_intro")
    Map<String, Object> alterForumIntro(@RequestBody Map<String, String> requestBody) {
        // 验证必需字段
        Map<String, Object> validationError = validateRequiredFields(requestBody, "forumId", "newIntro");
        if (validationError != null) {
            return validationError;
        }

        try {
            Integer forumId = Integer.parseInt(requestBody.get("forumId"));
            String newIntro = requestBody.get("newIntro");
            Forum forum = forumMapper.getForumById(forumId);

            if (forum == null) {
                return Map.of("success", false, "message", "论坛不存在");
            }

            forum.setIntroduction(newIntro);
            forumMapper.updateForum(forum);

            return Map.of("success", true);

        } catch (NumberFormatException e) {
            // Handle the case where parsing to integer fails
            logger.error("Invalid forum ID format: {}", e.getMessage());
            return Map.of("success", false, "message", "论坛ID格式错误");
        } catch (Exception e) {
            logger.error("error in endpoint /alter_forum_intro: {}", e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @GetMapping("/author_of_the_post")
    public ReturnUser getAuthor(@RequestParam Integer postId) {
        int authorId = userMapper.getAuthorIdByPostId(postId);
        User user = userMapper.getUserById(authorId);
        ReturnUser returnUser = new ReturnUser(user.getId(), user.getUserName(), user.getAvatarUrl(), user.getSelfIntro(),
                user.getFanNum(), user.getSubscribeNum(), user.getPhoneNum(), user.getAge());
        return returnUser;
    }

}