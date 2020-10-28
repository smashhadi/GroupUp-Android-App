package ece651.adapter;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ece651.Model.Post;
import ece651.Model.PostUser;
import ece651.groupup.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> postList = new ArrayList<>();

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(View v) {
            super(v);
        }

        @Override
        public void onClick(View view) {
            //TODO: add on click functionality
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Post post = postList.get(position);
        View postItem = holder.itemView;

        ImageView thumbnail = postItem.findViewById(R.id.post_thumbnail);
        thumbnail.setImageResource(R.drawable.ic_profile_pic);

        TextView user_name = postItem.findViewById(R.id.post_name);
        PostUser user = post.user;
        String full_name = user.firstName + " " + user.lastName;
        user_name.setText(full_name);

        TextView post_time = postItem.findViewById(R.id.post_time);
        post_time.setText(post.postTime.toString());

        TextView post_msg = postItem.findViewById(R.id.post_msg);
        post_msg.setText(post.msg);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return postList.size();
    }
}
