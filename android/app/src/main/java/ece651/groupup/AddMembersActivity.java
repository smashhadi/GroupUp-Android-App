package ece651.groupup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;


import java.util.LinkedList;
import java.util.List;
import ece651.Model.GroupMember;

public class AddMembersActivity extends AppCompatActivity {

    private Button btnAddTag;
    private Button btnSubmit;
    private String acceptedReq = "No";
    private String acceptedReq_user = "Yes";

    private List<String> emailIDs = new LinkedList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        Intent intent = getIntent();
        final String groupId = intent.getStringExtra("group_id");
        //Bundle from_create_group_activity = getIntent().getExtras();
        //final String groupId = from_create_group_activity.getString("group_id");
        final String groupName = intent.getStringExtra("group_name");

        TextView txtView = (TextView) findViewById(R.id.activity_add_members_group_name);
        txtView.setText(groupName);

        btnAddTag = (Button)findViewById(R.id.activity_add_members_btn_add_email);
        btnSubmit = (Button)findViewById(R.id.activity_add_members_btn_submit);

        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createATextView(emailIDs);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailIDs.clear();
                //groupName = ((EditText)findViewById(R.id.activity_create_group_group_name)).getText().toString();

                LinearLayout tagLayout = (LinearLayout)findViewById(R.id.activity_add_members_add_email_layout);
                int numTags = tagLayout.getChildCount();

                for(int i=0; i<numTags; i++){
                    LinearLayout l = (LinearLayout)tagLayout.getChildAt(i);
                    EditText etTag = (EditText) l.getChildAt(0);
                    String tag = etTag.getText().toString();

                    if(tag != null && !tag.isEmpty()){
                        emailIDs.add(tag);
                    }
                }

                //String userId = FirebaseAuth.getInstance().getUid() ;
                String userId = "user1";
                FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                CollectionReference memberCollection = firestoreDB.collection("GroupMembers");
                DocumentReference newMember = memberCollection.document();
                GroupMember memberList = new GroupMember(groupId, userId, acceptedReq_user);
                newMember.set(memberList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddMembersActivity.this, "Successful!!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddMembersActivity.this, "Fail: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    protected void createATextView(final List tagsList) {

        LayoutParams lparams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout tagLayout = (LinearLayout) findViewById(R.id.activity_add_members_add_email_layout);
        final LinearLayout newTagLayout = new LinearLayout(this);
        newTagLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        newTagLayout.setOrientation(LinearLayout.HORIZONTAL);

        final EditText et = new EditText(this);
        Button btn = new Button(this);
        btn.setText("Remove");
        btn.setBackgroundResource(0);

        et.setLayoutParams(lparams);
        btn.setLayoutParams(lparams);

        newTagLayout.addView(et,0);
        newTagLayout.addView(btn,1);
        tagLayout.addView(newTagLayout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout l = (LinearLayout)view.getParent();
                LinearLayout parent = (LinearLayout)l.getParent();
                String tag = ((EditText)l.getChildAt(0)).getText().toString();
                tagsList.remove(tag);
                parent.removeView(l);
            }
        });

    }
}
