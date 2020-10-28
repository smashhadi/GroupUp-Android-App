package ece651.Model;

import java.util.List;

public class Group{
    public String id;
    public String name;
    public List<String> tags;
    public String status;
    public String visibility;
    public String location;

    public Group(){

    }

    public Group(String p_id, String p_name) {
        id = p_id;
        name = p_name;
    }

    public Group(String name, List<String> tag, String stat, String vis, String locn) {
        this.name = name;
        this.tags = tag;
        this.status = stat;
        this.visibility = vis;
        this.location = locn;
    }
}
