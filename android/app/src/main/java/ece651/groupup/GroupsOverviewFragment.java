package ece651.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ece651.Model.Group;
import ece651.adapter.GroupsAdapter;

public class GroupsOverviewFragment extends Fragment {

    private ArrayList<Group> groups = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_groups_overview, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler);
        mAdapter = new GroupsAdapter(getContext(), groups);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        ));

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateGroupActivity.class));
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //TODO: user is not logged in; return to log in screen
            //For now use known user id
            getGroupIDsFor("rrLxruURdnWXkWhQ5CqpAvviMu72");
        } else {
            getGroupIDsFor(user.getUid());
        }

        return rootView;
    }

    private void getGroupIDsFor(String user) {
        //TODO: move database calls to specialized module
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("GroupMembers")
                .whereEqualTo("User",user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Set<String> groupIDs = new HashSet<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id = doc.get("Group").toString();
                                groupIDs.add(id);
                            }
                            getGroupNames(groupIDs);
                        } else {
                            Log.d("Database", "Failed to fetch groups the user belongs in.");
                        }
                    }
                });
    }

    private void getGroupNames(final Collection<String> groupIDs) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id = doc.getId();
                                if (groupIDs.contains(id)) {
                                    String name = doc.get("Name").toString();
                                    groups.add(new Group(id, name));
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}

