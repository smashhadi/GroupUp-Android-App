package ece651.groupup;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import ece651.Model.Post;
import ece651.adapter.PostAdapter;

public class FeedFragment extends Fragment {

    private static final String TAG = "FeedActivity_Debug";
    private RecyclerView recyclerView;
    private PostAdapter pAdapter;
    private ArrayList<Post> posts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_feed, container, false);

        recyclerView = rootView.findViewById(R.id.feed);
        pAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(pAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //TODO: user is not logged in; return to log in screen
            //For now use known user id
            fetchPosts("rrLxruURdnWXkWhQ5CqpAvviMu72");
        } else {
            fetchPosts(user.getUid());
        }
        return rootView;
    }

    private void fetchPosts(String user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .whereEqualTo("user.id", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                posts.add(doc.toObject(Post.class));
                            }
                            pAdapter.notifyDataSetChanged();
                            Log.d(TAG, "Dataset updated with posts " + Integer.toString(posts.size()));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
