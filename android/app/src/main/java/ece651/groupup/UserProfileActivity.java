package ece651.groupup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.TypedValue;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ece651.Model.User;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "GroupActivity_Debug";
    private User user;
    private TextView etName;
    private TextView etEmail;
    private TextView tvTagTitle;
    private TextView tvNoTags;
    private Button btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        etName = findViewById(R.id.activity_user_profile_tv_name);
        etEmail = findViewById(R.id.activity_user_profile_tv_email);
        tvTagTitle = findViewById(R.id.activity_user_profile_tv_tags);
        tvNoTags = findViewById(R.id.activity_user_profile_tv_no_tags);
        btnEdit = findViewById(R.id.activity_user_profile_btn_edit);

        String userId = FirebaseAuth.getInstance().getUid() ;
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("user")
                .document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            user = doc.toObject(User.class);

                            String name = user.getFirstName() + " " + user.getLastName();
                            String email = user.getEmail();
                            etName.setText(name);
                            etEmail.setText(email);
                            List<String> tags = user.getTags();

                            if(tags!=null && !tags.isEmpty()){
                                tvTagTitle.setVisibility(View.VISIBLE);
                                for(String tag : tags){
                                    createTagLayout(tag);

                                }

                            }else{
                                tvNoTags.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(UserProfileActivity.this, EditUserActivity.class));
            }
        });
    }

    protected void createTagLayout(String tag) {

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout tagLayout = (LinearLayout) findViewById(R.id.activity_user_profile_tags_layout);
        LinearLayout newTagLayout = new LinearLayout(this);
        newTagLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView tv = new TextView(this);

        tv.setLayoutParams(lparams);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        tv.setText("- " + tag);
        newTagLayout.addView(tv,0);
        tagLayout.addView(newTagLayout);

    }
}
