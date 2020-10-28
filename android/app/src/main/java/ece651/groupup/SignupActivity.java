package ece651.groupup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ece651.Model.User;



public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private  FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);


//        btnResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
//            }
//        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

//                if (TextUtils.isEmpty(email)){
//                    Toast.makeText(getApplicationContext(), "Enter Email address!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password)){
//                    Toast.makeText(getApplicationContext(),"Enter Password!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (password.length() < 6){
//                    Toast.makeText(getApplicationContext(),"Password too short!",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter Email address!", Toast.LENGTH_SHORT).show();
                    inputEmail.setError( "Email is required!" );
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Enter Password!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError( "Password is required!" );
                    return;
                }

                if (password.length() < 6){
                    Toast.makeText(getApplicationContext(),"Password too short!",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete"+task.isSuccessful(),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                if(!task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "Authentication Failed." + task.getException(), Toast.LENGTH_SHORT).show();
                                } else{


                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                    String userId = currentFirebaseUser.getUid();

                                    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                                    CollectionReference userCollection = firestoreDB.collection("user");
                                    DocumentReference newUser = userCollection.document(userId);
                                    User  user = new User(email);
                                    newUser.set(user);

                                    startActivity(new Intent(SignupActivity.this, ProfileSetupActivity.class));
                                }
                            }
                        });
            }
        });
    }
}
