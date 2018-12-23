package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Mytag";
    public static final String EXTRA_NMEC = "Nmec";
    public static final String EXTRA_HASCOURSES = "HasCourses";

    private EditText mEmailText;
    private EditText mPasswordText;

    private Button mLoginButton;
    private Button mSignUpButton;
    private String email;
    private String nmec;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


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
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {
        email = mEmailText.getText().toString();
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


    private void startMainActivity(FirebaseUser user) {

        Log.d("MyTag3", user.getEmail());
        retrieveUserNmec(user.getEmail());

    }

    private void retrieveUserNmec(final String curEmail) {
        db.collection("Students").whereEqualTo("UaEmail", curEmail)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            Log.d("MyTag4", key);
                            if (key.equals("Nmec")) {
                                nmec = value.toString();
                                continueToMainActivity();
                            }
                        }
                    } else {
                        Log.d("MyTag3", "Document doesn't exist");
                    }
                }
            }
        });
    }

    private void continueToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);

/*        intent.putExtra(EXTRA_NMEC, nmec);
        String bl =  Boolean.toString(checkIfStudentHasCourses(nmec));
        intent.putExtra(EXTRA_HASCOURSES, bl);

        Log.d("MyTag3", "N mec: " + nmec);
        Log.d("MyTag3", bl);*/
        startActivity(intent);
        MainActivity.setNmec(nmec);
    }


    private boolean checkIfStudentHasCourses(String nmec) {
        final boolean[] hasCourses = new boolean[1];
        hasCourses[0]=false;
        db.collection("Students/St" + nmec + "/Courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                hasCourses[0]=true;
                                break;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return hasCourses[0];
    }
}
