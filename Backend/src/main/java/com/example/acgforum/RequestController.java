package com.example.acgforum;

import com.example.acgforum.ReturnFormat.ReturnComment;
import com.example.acgforum.ReturnFormat.ReturnPost;
import com.example.acgforum.ReturnFormat.ReturnSimplePost;
import com.example.acgforum.ReturnFormat.ReturnUser;
import com.example.acgforum.mappers.PostMapper;
import com.example.acgforum.mappers.UserMapper;
import com.example.acgforum.tables.Post;
import com.example.acgforum.tables.User;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class RequestController {
    @Resource(name = "postMapper")
    PostMapper postMapper;
    @Resource(name = "userMapper")
    UserMapper userMapper;

    @RequestMapping("/post/{id}")
    public ReturnPost post(@PathVariable Integer id) {
        Post p = postMapper.getById(id);
        User author = userMapper.getUserById(p.getAuthorId());
        ArrayList<Integer> arr = postMapper.subPostList(id);
        ReturnPost rp = new ReturnPost(p.getId(), p.getAuthorId(), author.getUserName()
                , p.getTitle(), p.getDate(), p.getContent(), p.getLikeNum(), p.getCommentNum(), arr);

        return rp;
    }

    @RequestMapping("/most_liked_post")
    public ArrayList<ReturnSimplePost> mostLikedPost(@RequestBody Integer page, @RequestBody String interval) {
        ArrayList<ReturnSimplePost> Returnlist = new ArrayList<>();
        ArrayList<Post> postList = new ArrayList<>();

        if (interval.equalsIgnoreCase("week"))
            postList = postMapper.weeklyMostLikedPostList(30, (page - 1) * 30);
        else if (interval.equalsIgnoreCase("month"))
            postList = postMapper.monthlyMostLikedPostList(30, (page - 1) * 30);
        else
            postList = postMapper.mostLikedPostList(30, (page - 1) * 30);

        // cast "Post" into "ReturnSimplePost".
        postList.forEach(i -> {
            Returnlist
                    .add(new ReturnSimplePost(i.getId(), i.getTitle(), i.getDate(), i.getLikeNum(), i.getCommentNum()));
        });

        return Returnlist;
    }

    @RequestMapping("/latest_post/{page}")
    public ArrayList<ReturnSimplePost> latestPost(@PathVariable Integer page) {
        ArrayList<ReturnSimplePost> ReturnList = new ArrayList<>();

        postMapper.latestPostList(30, (page - 1) * 30).forEach(i -> {
            ReturnList.add(new ReturnSimplePost(i.getId(), i.getTitle(),
                    i.getDate(), i.getLikeNum(), i.getCommentNum()));
        });
        return ReturnList;
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
        ArrayList<ReturnSimplePost> returnList = new ArrayList<>();
        ArrayList<Post> postList = postMapper.collectedPostListByUserIdLimit(userId, 30, (page - 1) * 30);

        postList.forEach(i -> {
            returnList.add(new ReturnSimplePost(
                    i.getId(), i.getTitle(), i.getDate(), i.getLikeNum(), i.getCommentNum()));
        });
        return returnList;
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
        boolean isRoot = requestBody.get("isRoot").equalsIgnoreCase("true");
        Integer root = null; // Declare the variable here

        if (!isRoot) {
            root = Integer.parseInt(requestBody.get("root"));
            Post p = postMapper.getById(root);
            p.setCommentNum(p.getCommentNum() + 1);
            postMapper.update(p);
        }
        Post p = new Post(null, Integer.parseInt(requestBody.get("userId")), isRoot, root, requestBody.get("title"),
                LocalDateTime.now(), 0, 0, requestBody.get("content"));

        postMapper.insertPost(p);
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
        ArrayList<ReturnUser> ReturnUsers = new ArrayList<>();

        subscribeUsers.forEach(i -> {
            ReturnUsers.add(new ReturnUser(true, i.getId(), i.getUserName(),
                    i.getAvatarUrl(), i.getSelfIntro(), i.getFanNum(), i.getSubscribeNum()));
            System.out.println(i.toString());
        });

        return subscribeUsers;
    }

    @GetMapping("/posts_by_author")
    public ArrayList<ReturnSimplePost> getPostsByAuthor(@RequestParam Integer authorId) {

        ArrayList<Post> postList = postMapper.getByAuthor(authorId);

        //if is comment.
        postList.forEach(i -> {
            if (i.getTitle() == null) {
                if (i.getContent().length() > 50)
                    i.setTitle(i.getContent().substring(0, 50) + "...");
                else
                    i.setTitle(i.getContent());
            }
        });

        ArrayList<ReturnSimplePost> Returnlist = new ArrayList<>();
        // cast "Post" into "ReturnSimplePost".
        postList.forEach(i -> {
            Returnlist.add(new ReturnSimplePost(i.getId(),
                    i.getTitle(), i.getDate(), i.getLikeNum(), i.getCommentNum()));
        });
        return Returnlist;
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

    @GetMapping("/search_post")
    public ArrayList<ReturnSimplePost> searchPost(@RequestParam String keyword) {
        ArrayList<Post> pl = postMapper.searchKeyword(keyword);

        pl.sort((a, b) -> {
            int count_a = countKeywordOccurrences(a.getTitle() + a.getContent(), keyword);
            int count_b = countKeywordOccurrences(b.getTitle() + b.getContent(), keyword);
            return count_b - count_a;
        });
        // cast "Post" into "ReturnSimplePost".
        ArrayList<ReturnSimplePost> rl = new ArrayList<>();
        pl.forEach(i -> {
            rl.add(new ReturnSimplePost(i.getId(),
                    i.getTitle(), i.getDate(), i.getLikeNum(), i.getCommentNum()));
        });
        return rl;
    }

    @GetMapping("/search_user")
    ArrayList<User> searchUser(@RequestParam String keyword) {
        ArrayList<User> ul = userMapper.searchUsers(keyword);
        return ul;
    }

    @RequestMapping("/unsubscribe_user")
    public void unsubscribe_user(@RequestParam Integer myId, @RequestParam Integer userId) {
        userMapper.deleteSubscribedUser(userId, myId);
    }

    @RequestMapping("/uncollect_post")
    public void uncollect_post(@RequestParam Integer userId, @RequestParam Integer postId) {
        userMapper.deleteCollectedPost(userId, postId);
    }
}
