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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signup);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progress);
        btnResetPassword = findViewById(R.id.btn_reset_password);


        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

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

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(!task.isSuccessful()){
                            if(password.length() < 6){
                                inputPassword.setError(getString(R.string.minimum_password));
                            }else{
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT);
                            }
                        }else {
                            startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                        }
                    }
                });
            }
        });
    }
}
