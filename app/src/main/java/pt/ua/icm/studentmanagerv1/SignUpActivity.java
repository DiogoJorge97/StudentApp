package pt.ua.icm.studentmanagerv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "MyTagAC";

    private EditText nameEdit;
    private EditText uaEmailEdit;
    private EditText passwordEdit;
    private EditText gmailEdit;
    private EditText nMecEdit;
    private Button sigUpButton;

    ArrayList<String> CoursesToPrint;
    Spinner spinner;


    ArrayList<String[]> possibleItems;


    private static final String KEY_NAME = "name";
    private static final String KEY_UA = "ua email";
    private static final String KEY_GMAIL = "gmail";
    private static final String KEY_NMEC = "nmec";
    private static final String KEY_DEGREE = "deggres";



    String studentDegree;


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
        spinner = findViewById(R.id.degree_spinner);
        sigUpButton = findViewById(R.id.sign_up_button);
        possibleItems = new ArrayList<>();


        lookInFirestore();
        sigUpListener();


    }

    private void spinner() {
        Spinner spinner = findViewById(R.id.degree_spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CoursesToPrint);
        spinner.setAdapter(spinnerArrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String choosenDegree = parent.getItemAtPosition(position).toString();
                for (String[] degree: possibleItems){
                    if(degree[1].equals(choosenDegree)){
                        studentDegree = degree[0];
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


    //Sign Up Button On Click
    private void sigUpListener() {
        sigUpButton.setOnClickListener(new View.OnClickListener() {
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

    //Save data to firestore
    private void addUserToDataBase(String name, String uaEmail, String gmail, final int nMec) {
        Map<String, Object> subMap = new HashMap<>();
        subMap.put(MainActivity.getCurrentYear(), studentDegree);
        Map<String, Object> userData = new HashMap<>();
        userData.put(KEY_NAME, name);
        userData.put(KEY_UA, uaEmail);
        userData.put(KEY_GMAIL, gmail);
        userData.put(KEY_NMEC, nMec);
        userData.put(KEY_DEGREE, subMap);

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


    //Methods to retrieve available Degrees
    private void lookInFirestore() {
        MainActivity.getDb().collection("Degrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    getAllCourses(task);
                    createArrayOfCourses(possibleItems);
                    CoursesToPrint = createArrayOfCourses(possibleItems);
                    spinner();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    private ArrayList<String> createArrayOfCourses(ArrayList<String[]> possibleItems) {
        ArrayList<String> listItems = new ArrayList<>();
        for (String[] item : possibleItems) {
            listItems.add(item[1]);
        }
        return listItems;
    }
    private void getAllCourses(@NonNull Task<QuerySnapshot> task) {
        for (QueryDocumentSnapshot document : task.getResult()) {
            String[] degreeData = new String[2];
            degreeData[0] = document.getId();
            degreeData[1] = retrieveNeededData(document.getData());
            Log.d(TAG, Arrays.toString(degreeData));
            possibleItems.add(degreeData);
        }
    }
    private String retrieveNeededData(Map<String, Object> data) {
        String type = "";
        String name = "";
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();


            if (key.equals("Type")) {
                type = value.toString();
            }
            if (key.equals("Name")) {
                name = value.toString();
            }
        }
        return (type + " " + name);

    }


}
