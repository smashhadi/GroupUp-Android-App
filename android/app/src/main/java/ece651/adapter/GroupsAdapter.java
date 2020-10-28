package ece651.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ece651.Model.Group;
import ece651.groupup.GroupActivity;
import ece651.groupup.GroupsOverviewFragment;
import ece651.groupup.R;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
    private List<Group> dataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textView;
        public String groupId;
        private Context context;

        public ViewHolder(View v, Context context) {
            super(v);
            this.context = context;
            this.textView = v.findViewById(R.id.gListItem);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, GroupActivity.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("groupName", textView.getText());

            context.startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GroupsAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.dataset = groups;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.group_list_item, parent, false);
        return new ViewHolder(v, this.context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Group group = dataset.get(position);
        holder.textView.setText(group.name);
        holder.groupId = group.id;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
