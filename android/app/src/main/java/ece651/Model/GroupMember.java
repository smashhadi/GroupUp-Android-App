package ece651.Model;

public class GroupMember {

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAcceptedRequest() {
        return acceptedRequest;
    }

    public void setAcceptedRequest(String acceptedRequest) {
        this.acceptedRequest = acceptedRequest;
    }

    private String group_id;
    private String user_id;
    private String acceptedRequest;

    public GroupMember(String GID, String UID, String Acc){
        this.group_id = GID;
        this.user_id = UID;
        this.acceptedRequest = Acc;
    }
}
