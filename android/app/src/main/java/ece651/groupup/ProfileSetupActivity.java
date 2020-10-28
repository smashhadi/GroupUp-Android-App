package ece651.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;


public class ProfileSetupActivity extends AppCompatActivity {
    private Button btnAddTag;
    private Button btnSubmit;
    private String firstName;
    private String lastName;

    private List<String> tags = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnAddTag = findViewById(R.id.activity_profile_setup_btn_add_tags);
        btnSubmit = findViewById(R.id.activity_profile_setup_btn_submit);

        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTagView(tags);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags.clear();
                firstName = ((EditText)findViewById(R.id.activity_profile_setup_et_fname)).getText().toString();
                lastName = ((EditText)findViewById(R.id.activity_profile_setup_et_lname)).getText().toString();
                LinearLayout tagLayout = (LinearLayout)findViewById(R.id.activity_profile_setup_tags_layout);
                int numTags = tagLayout.getChildCount();

                for(int i=0; i<numTags; i++){
                    LinearLayout l = (LinearLayout)tagLayout.getChildAt(i);
                    EditText etTag = (EditText) l.getChildAt(0);
                    String tag = etTag.getText().toString();

                    if(tag != null && !tag.isEmpty()){
                        tags.add(tag);
                    }
                }

                String userId = FirebaseAuth.getInstance().getUid() ;

                FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                firestoreDB.collection("user")
                        .document(userId)
                        .update("firstName", firstName, "lastName", lastName, "tags",tags);
                startActivity(new Intent(ProfileSetupActivity.this, LoginActivity.class));
            }
        });
    }

    protected void createTagView(final List tagsList) {

        LayoutParams lparams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout tagLayout = (LinearLayout) findViewById(R.id.activity_profile_setup_tags_layout);
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
