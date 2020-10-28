package ece651.Model;

public class PostUser {
    public String id;
    public String firstName;
    public String lastName;

    public PostUser(String userId, String firstName, String lastName) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PostUser() {
        // Default constructor required for calls to DataSnapshot.getValue(PostUser.class)
    }
}
