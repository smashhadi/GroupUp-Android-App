package ece651.Model;

import java.util.Date;

public class Post {

    public String groupId;
    public PostUser user;
    public Date postTime;
    public String msg;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String groupId, String userId, String firstName, String lastName, Date postTime, String msg) {
        this.groupId = groupId;
        this.user = new PostUser(userId, firstName, lastName);
        this.postTime = postTime;
        this.msg = msg;
    }

    public Post(String groupId, PostUser user, Date postTime, String msg) {
        this.groupId = groupId;
        this.user = user;
        this.postTime = postTime;
        this.msg = msg;
    }
}
