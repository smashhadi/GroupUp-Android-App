package ece651.groupup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.content.Intent;
import android.text.TextUtils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

import ece651.Model.Group;

public class CreateGroupActivity extends AppCompatActivity {

    private Button btnAddTag;
    private Button btnSubmit;
    private String groupName;
    private String status = "Active";
    private String visibility = "Private";
    private String location;
    private String groupId;

    private List<String> tags = new LinkedList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);


        btnAddTag = (Button)findViewById(R.id.activity_create_group_btn_add_tags);
        btnSubmit = (Button)findViewById(R.id.activity_create_group_btn_submit);

        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createATextView(tags);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags.clear();
                groupName = ((EditText)findViewById(R.id.activity_create_group_group_name)).getText().toString();
                location = ((EditText)findViewById(R.id.activity_create_group_location)).getText().toString();

                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(getApplicationContext(), "Enter Group Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                LinearLayout tagLayout = (LinearLayout)findViewById(R.id.activity_create_group_tags_layout);
                int numTags = tagLayout.getChildCount();

                for(int i=0; i<numTags; i++){
                    LinearLayout l = (LinearLayout)tagLayout.getChildAt(i);
                    EditText etTag = (EditText) l.getChildAt(0);
                    String tag = etTag.getText().toString();

                    if(tag != null && !tag.isEmpty()){
                        tags.add(tag);
                    }
                }

                Switch status_switch = (Switch) findViewById(R.id.activity_create_group_status_switch);
                Switch vis_switch = (Switch) findViewById(R.id.activity_create_group_visibility_switch);

                status_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            status = "Active";
                        } else {
                            status = "Inactive";
                        }
                    }
                });

                vis_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            visibility = "Public";
                        } else {
                            visibility = "Private";
                        }
                    }
                });

                FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                CollectionReference groupCollection = firestoreDB.collection("Groups");
                DocumentReference newGroup = groupCollection.document();
                Group currentGroup = new Group(groupName, tags, status, visibility, location);
                newGroup.set(currentGroup);

                //Get Group ID of newly created group
                newGroup.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        //if (e != null) {
                         //   System.err.println("Listen failed: " + e);
                         //   return;
                        //}

                        if (snapshot != null && snapshot.exists()) {
                            //System.out.println("Current Group ID: " +
                            groupId = snapshot.getId();
                            Intent intent = new Intent(CreateGroupActivity.this, AddMembersActivity.class);
                            intent.putExtra("group_id", groupId);
                            intent.putExtra("group_name", groupName);
                            startActivity(intent);
                        }
                        //else {
                         //   System.out.print("Current data: null");
                        //}
                    }
                });

            }
        });

    }

    protected void createATextView(final List tagsList) {

        LayoutParams lparams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout tagLayout = (LinearLayout) findViewById(R.id.activity_create_group_tags_layout);
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
