package com.example.acgforum;

import com.example.acgforum.ReturnFormat.*;
import com.example.acgforum.mappers.ForumMapper;
import com.example.acgforum.mappers.PostMapper;
import com.example.acgforum.mappers.UserMapper;
import com.example.acgforum.tables.Forum;
import com.example.acgforum.tables.Post;
import com.example.acgforum.tables.User;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class RequestController {
    Logger logger = LoggerFactory.getLogger("CONTROLLER'S LOGGER");
    @Value("${nginx.archiveLocation}")
    public static String archiveLocation;
    @Resource(name = "postMapper")
    PostMapper postMapper;
    @Resource(name = "userMapper")
    UserMapper userMapper;
    @Resource(name = "forumMapper")
    ForumMapper forumMapper;

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

            rl.add(new ReturnSimplePost(i.getId(),
                    i.getTitle(), i.getDate(), i.getLikeNum(), i.getCommentNum()));
        }

        return rl;
    }


    @RequestMapping("/post/{id}")
    public ReturnPost post(@PathVariable Integer id) {
        Post p = postMapper.getById(id);
        User author = userMapper.getUserById(p.getAuthorId());
        ArrayList<Integer> arr = postMapper.subPostList(id);

        return new ReturnPost(p.getId(), "success", p.getAuthorId(), author.getUserName(),
                p.getTitle(), p.getDate(), p.getContent(), p.getLikeNum(), p.getCommentNum(), arr);
    }

    @RequestMapping("/most_liked_post")
    public ArrayList<ReturnSimplePost> mostLikedPost(@RequestBody Integer page, @RequestBody String interval) {
        ArrayList<Post> postList;

        if (interval.equalsIgnoreCase("week"))
            postList = postMapper.weeklyMostLikedPostList(30, (page - 1) * 30);
        else if (interval.equalsIgnoreCase("month"))
            postList = postMapper.monthlyMostLikedPostList(30, (page - 1) * 30);
        else
            postList = postMapper.mostLikedPostList(30, (page - 1) * 30);

        return getReturnSimplePosts(postList);
    }

    @RequestMapping("/latest_post/{page}")
    public ArrayList<ReturnSimplePost> latestPost(@PathVariable Integer page) {
        return getReturnSimplePosts(postMapper.latestPostList(30, (page - 1) * 30));
    }

    @RequestMapping(value = "user/{id}")
    public ReturnUser user(@PathVariable Integer id) {
        User u = userMapper.getUserById(id);
        if (u == null)
            return new ReturnUser(false);

        if (u.getAvatarUrl() == null)
            u.setAvatarUrl("defaultAvatar.png");
        ReturnUser returnUser = new ReturnUser(true, id, u.getUserName(), u.getAvatarUrl(),
                u.getSelfIntro(), u.getFanNum(), u.getSubscribeNum());

        return returnUser;
    }

    @RequestMapping("collected_posts")
    public ArrayList<ReturnSimplePost> collectedPosts(@RequestParam Integer page, @RequestParam Integer userId) {
        ArrayList<Post> postList = postMapper.collectedPostListByUserIdLimit(userId, 30, (page - 1) * 30);
        return getReturnSimplePosts(postList);
    }

    @RequestMapping("fans/{myId}")
    public ArrayList<User> fansById(@PathVariable Integer myId) {
        return userMapper.getFansById(myId);
    }

    @RequestMapping("/signup")
    public Map<String, Object> signup(@RequestBody Map<String, String> requestBody) {
        System.out.println(requestBody.toString());
        boolean isLegalEmail = userMapper.isLegalEmail(requestBody.get("email"));

        // store user into database.
        if (isLegalEmail) {
            User newUser = new User();
            newUser.setUserName(requestBody.get("userName"));
            newUser.setEmail(requestBody.get("email"));
            newUser.setHc_password(requestBody.get("password").hashCode());

            userMapper.insertUser(newUser);
        }

        // Return message.
        Map<String, Object> returnObj = new HashMap<>();
        returnObj.put("isLegalEmail", isLegalEmail);
        returnObj.put("isLegalUserName", true);
        return returnObj;
    }

    @RequestMapping("/put_post")
    public void putPost(@RequestBody Map<String, String> requestBody) {
        try {
            boolean isRoot = requestBody.get("isRoot").equalsIgnoreCase("true");
            Integer root = null; // Declare the variable here

            if (!isRoot) /*if this post was a comment.*/ {
                //comments number + 1
                root = Integer.parseInt(requestBody.get("root"));
                Post p = postMapper.getById(root);
                p.setCommentNum(p.getCommentNum() + 1);
                postMapper.update(p);
            }
            Post p = new Post(null, Integer.parseInt(requestBody.get("userId")), isRoot, root
                    , requestBody.get("title"), LocalDateTime.now(), 0, 0
                    , requestBody.get("content"), Integer.parseInt(requestBody.get("forumId")));

            postMapper.insertPost(p);

        } catch (NumberFormatException | NullPointerException e) {
            // Handle the case where parsing to integer fails or requestBody is null
            System.out.println("Invalid user ID format or null request body: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    @RequestMapping("/login")
    public ReturnUser login(@RequestBody Map<String, String> jsonBody) {
        String email = jsonBody.get("email");
        // String userName = jsonBody.get("userName");
        Integer hc_password = jsonBody.get("password").hashCode();

        Boolean token = userMapper.loginByEmail(email, hc_password);

        if (!token)
            return new ReturnUser(false);
        else {
            User user = userMapper.getUserByEmail(email);
            return new ReturnUser(true, user.getId(), user.getUserName(), user.getAvatarUrl(), user.getSelfIntro(),
                    user.getFanNum(), user.getSubscribeNum());
        }
    }

    @RequestMapping("delete_post/{id}")
    public void deletePost(@PathVariable Integer id) {
        Post post = postMapper.getById(id);
        if (post == null)
            return;

        if (post.getIsRoot()) {
            ArrayList<Integer> subPostList = postMapper.subPostList(id);
            subPostList.forEach(i -> {
                postMapper.deletePostById(i);
            });
        }

        postMapper.deletePostById(id);
    }

    @RequestMapping("/delete_user/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userMapper.deleteUserById(id);
    }

    @RequestMapping("like_post")
    public Map<String, Boolean> likePost(@RequestParam Integer userId, @RequestParam Integer postId) {

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

    @RequestMapping("collect_post")
    public Map<String, Boolean> collectPost(@RequestParam Integer userId, @RequestParam Integer postId) {
        if (userMapper.isCollected(userId, postId)) {
            return Map.of("isOk", false);
        }

        userMapper.insertCollectedPost(userId, postId);
        return Map.of("isOk", true);
    }

    @RequestMapping("subscribe_user")
    public void subscribeUser(@RequestParam Integer myId, @RequestParam Integer userId) {
        userMapper.insertSubscribedUser(userId, myId);
    }

    @RequestMapping("subposts")
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

    @RequestMapping("/subscribed_users")
    public ArrayList<User> subscribedUsers(@RequestParam Integer id, @RequestParam Integer page) {

        ArrayList<User> subscribeUsers = userMapper.getSubscribedUsersLimit(id, 30, 30 * (page - 1));
//        ArrayList<ReturnUser> ReturnUsers = new ArrayList<>();
//
//        subscribeUsers.forEach(i -> {
//            ReturnUsers.add(new ReturnUser(true, i.getId(), i.getUserName(),
//                    i.getAvatarUrl(), i.getSelfIntro(), i.getFanNum(), i.getSubscribeNum()));
//        });

        return subscribeUsers;
    }

    @GetMapping("/posts_by_author")
    public ArrayList<ReturnSimplePost> getPostsByAuthor(@RequestParam Integer authorId) {
        ArrayList<Post> postList = postMapper.getByAuthor(authorId);
        return getReturnSimplePosts(postList);
    }


    @GetMapping("/search_post")
    public ArrayList<ReturnSimplePost> searchPost(@RequestParam String keyword) {
        ArrayList<Post> pl = postMapper.searchKeyword(keyword);

        pl.sort((a, b) -> {
            int count_a = countKeywordOccurrences(a.getTitle() + a.getContent(), keyword);
            int count_b = countKeywordOccurrences(b.getTitle() + b.getContent(), keyword);
            return count_b - count_a;
        });
        // cast "Post" into "ReturnSimplePost".
        return getReturnSimplePosts(pl);
    }


    @GetMapping("/search_user")
    ArrayList<User> searchUser(@RequestParam String keyword) {
        return userMapper.searchUsers(keyword);
    }

    @RequestMapping("/unsubscribe_user")
    public void unsubscribe_user(@RequestParam Integer myId, @RequestParam Integer userId) {
        userMapper.deleteSubscribedUser(userId, myId);
    }

    @RequestMapping("/uncollect_post")
    public void uncollect_post(@RequestParam Integer userId, @RequestParam Integer postId) {
        userMapper.deleteCollectedPost(userId, postId);
    }

    @RequestMapping("/forums/{page}")
    public ArrayList<ReturnForum> forums(@PathVariable Integer page) {

        PageHelper.startPage(page, 30);
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
        return returnList;
    }

    @GetMapping("/posts_by_forum")
    public ArrayList<ReturnSimplePost> postsByForum(Integer forumId, Integer page) {
        PageHelper.startPage(page, 30);
        ArrayList<Post> posts = forumMapper.getLatestPostListByForumId(forumId);
        return getReturnSimplePosts(posts);
    }

    @PostMapping("/create_forum")
    public Map<String, Object> createForum(@RequestBody Map<String, String> requestBody) {
        Forum queryForum = forumMapper.getForumByName(requestBody.get("forumName"));
        Map<String, Object> rm = new HashMap<>();

        if (queryForum == null) {
            Forum f = new Forum();
            f.setName(requestBody.get("forumName"));
            f.setIntroduction(requestBody.get("introduction"));
            f.setOwner(Integer.parseInt(requestBody.get("owner")));
            f.setPostNum(0);
            f.setUserNum(0);
            f.setIntroduction("defaultIcon.jpg");
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

    @PostMapping("/change_avatar/{userId}")
    Map<String, Object> changeAvatar(@PathVariable Integer userId, MultipartFile avatarFile) {
        //authenticate user
        if (userMapper.getUserById(userId) == null) {
            Map<String, Object> rm = new HashMap<>();
            rm.put("isOk", false);
            rm.put("message", "Unauthorized");
            return rm;
        }

        try /*store the file into nginx server.*/ {
            int lastDot = avatarFile.getOriginalFilename().lastIndexOf('.');
            String extname = avatarFile.getOriginalFilename().substring(lastDot);

            String fileName = UUID.randomUUID() + "_" + userId + extname;
            File fLocation = new File("/www/wwwroot/47.106.126.183_5555/" + fileName);

            if (!fLocation.exists())
                fLocation.createNewFile();

            avatarFile.transferTo(fLocation);
            logger.info("FILE PATH: " + fLocation.getAbsolutePath());

            User u = userMapper.getUserById(userId);
            u.setAvatarUrl(fileName);
            userMapper.updateUser(u);

        } catch (Exception e) {
            logger.error(e.getMessage());
            Map<String, Object> rm = new HashMap<>();
            rm.put("isOk", false);
            rm.put("message", "Failed to upload avatar");
            return rm;
        }

        Map<String, Object> rm = new HashMap<>();
        rm.put("isOk", true);
        return rm;
    }


    @PostMapping("/change_icon/{forumId}")
    Map<String, Object> changeIcon(@PathVariable Integer forumId, MultipartFile iconFile) {
        //authenticate user
        if (forumMapper.getForumById(forumId) == null) {
            Map<String, Object> rm = new HashMap<>();
            rm.put("isOk", false);
            rm.put("message", "forum don't exist.");
            return rm;
        }

        try /*store the file into nginx server.*/ {
            int lastDot = iconFile.getOriginalFilename().lastIndexOf('.');
            String extname = iconFile.getOriginalFilename().substring(lastDot);

            String fileName = UUID.randomUUID() + extname;
            File fLocation = new File("/www/wwwroot/47.106.126.183_5555/" + fileName);
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

    @GetMapping("/forum/{id}")
    Forum getForumById(@PathVariable Integer id) {
        return forumMapper.getForumById(id);
    }


    @PutMapping("alter_user_intro")
    void alterUserIntro(@RequestBody Map<String, String> requestBody) {
        try {
            Integer userId = Integer.parseInt(requestBody.get("userId"));
            String newIntro = requestBody.get("newIntro");
            User u = userMapper.getUserById(userId);

            assert newIntro != null;
            assert u != null;
            u.setSelfIntro(newIntro);
            userMapper.updateUser(u);

        } catch (NumberFormatException | NullPointerException e) {
            // Handle the case where parsing to integer fails or requestBody is null
            logger.error("Invalid user ID format or null request body: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PutMapping("/alter_forum_intro")
    void alterForumIntro(@RequestBody Map<String, String> requestBody) {
        try {
            Integer forumId = Integer.parseInt(requestBody.get("forumId"));
            String newIntro = requestBody.get("newIntro");
            Forum forum = forumMapper.getForumById(forumId);
            if (forum == null || newIntro == null)
                logger.error("forum or introduction were null.");

            assert forum != null;
            forum.setIntroduction(newIntro);
            forumMapper.updateForum(forum);


        } catch (NumberFormatException | NullPointerException e) {
            // Handle the case where parsing to integer fails or requestBody is null
            logger.error("Invalid forum ID format or null request body: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("error in endpoint /alter_forum_intro: {}", e.getMessage());
        }
    }



}
