package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailText;
    private EditText mPasswordText;

    private Button mLoginButton;
    private Button mSignUpButton;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initializing stuff
        setContentView(R.layout.activity_login);
        mEmailText = findViewById(R.id.loginTextView);
        mPasswordText = findViewById(R.id.passwordTextView);
        mLoginButton = findViewById(R.id.buttonLonIn);
        mSignUpButton = findViewById(R.id.buttonSignUp);
        mAuth = FirebaseAuth.getInstance();


        //Check if logIn is already done
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    startMainActivity(currentUser);
                }
            }
        };

        //check if logIn button has been pressed
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();

            }
        });

        //check if signUp button has been pressed
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUp();

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty((password))) {
            Toast.makeText(LoginActivity.this, "Filds are empty", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



    private void startSignUp() {
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty((password))) {
            Toast.makeText(LoginActivity.this, "Filds are empty", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void startMainActivity(FirebaseUser user) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

}
