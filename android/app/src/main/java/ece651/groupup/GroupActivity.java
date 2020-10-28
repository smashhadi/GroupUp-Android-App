package ece651.groupup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import ece651.Model.Post;
import ece651.adapter.PostAdapter;

public class GroupActivity extends AppCompatActivity {

    private static final String TAG = "GroupActivity_Debug";
    private RecyclerView recyclerView;
    private PostAdapter pAdapter;
    private ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");
        String groupName = intent.getStringExtra("groupName");
        setTitle(groupName);

        recyclerView = findViewById(R.id.post_list);
        pAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(pAdapter);
        fetchPosts(groupId, null);
    }

    private void fetchPosts(String groupId, Date time) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .whereEqualTo("groupId", groupId).get()
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
