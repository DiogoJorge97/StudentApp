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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "MyTag";

    private EditText nameEdit;
    private EditText uaEmailEdit;
    private EditText passwordEdit;
    private EditText gmailEdit;
    private EditText nMecEdit;

    private static final String KEY_NAME = "name";
    private static final String KEY_UA = "ua email";
    private static final String KEY_GMAIL = "gmail";
    private static final String KEY_NMEC = "nmec";


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();


        nameEdit = findViewById(R.id.name_edit);
        uaEmailEdit = findViewById(R.id.ua_email_edit);
        passwordEdit = findViewById(R.id.password_edit);
        gmailEdit = findViewById(R.id.gmail_edit);
        nMecEdit = findViewById(R.id.nmec_edit);


        final Button button = findViewById(R.id.sign_up_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startSignUp();
            }
        });

    }


    private void startSignUp() {
        final String name = nameEdit.getText().toString();
        final String uaEmail = uaEmailEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        final String gmail = gmailEdit.getText().toString();
        final int nMec = Integer.parseInt(nMecEdit.getText().toString());

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(uaEmail) || TextUtils.isEmpty(password) || TextUtils.isEmpty(gmail) || nMec == 0) {
            Log.d(TAG, nMec + " " + gmail + " " + password + " " + uaEmail + " " + name);
            Toast.makeText(SignUpActivity.this, "Filds are empty", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(uaEmail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                addUserToDataBase(name, uaEmail, gmail, nMec);
                            }
                        }
                    });
        }
    }

    private void addUserToDataBase(String name, String uaEmail, String gmail, final int nMec) {
        Map<String, Object> userData = new HashMap<>();
        userData.put(KEY_NAME, name);
        userData.put(KEY_UA, uaEmail);
        userData.put(KEY_GMAIL, gmail);
        userData.put(KEY_NMEC, nMec);

        db.document("/Students/St" + nMec).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra("NMEC", nMec);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
