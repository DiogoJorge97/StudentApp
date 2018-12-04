package pt.ua.icm.studentmanagerv1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponsibleNewCourse extends AppCompatActivity {


    //widgets
    Button mOpenDialog;
    Button mAddButton;
    EditText nameEdit;
    EditText abreviationEdit;
    EditText idEdit;
    EditText ectsEdit;

    //Firestore Keys
    static final String KEY_NAME = "Name";
    static final String KEY_ABRE = "Abbreviation";
    static final String KEY_ECTS = "ECTS";
    static final String KEY_ID = "ID";
    static final String KEY_EDITIONS = "Editions";
    public static final int TEXT_REQUEST = 1;
    private final String TAG = "DTag RespNCourse";

    private static ObjectStudent user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsible_new_course);


        mAddButton = findViewById(R.id.add_courses);
        mOpenDialog = findViewById(R.id.choseDegrees);
        nameEdit = findViewById(R.id.name_edit);
        abreviationEdit = findViewById(R.id.abr_edit);
        idEdit = findViewById(R.id.id_edit);
        ectsEdit = findViewById(R.id.ects_edit);


        mAddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveToFirestore();
            }
        });

        mOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResponsibleNewCourse.this, ResponsibleNewEdition.class);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        });


    }


    private void saveToFirestore() {
        Map<String, List<String>> editions = new HashMap<>();

        ArrayList<String> selected = ResponsibleNewEdition.getSelectedItems();
        if (selected.isEmpty()) {
            Toast.makeText(this, "Selecione uma ou mais edições", Toast.LENGTH_SHORT);
        } else {
            for (String elem : selected) {
                String yearTemp = elem.split(" ")[0];
                String semesterTemp = elem.split(" ")[1];
                Log.d(TAG, "YearTemp: " + yearTemp);
                Log.d(TAG, "SemesterTemp: " + semesterTemp);

                if (editions.get(yearTemp) != null) {
                    List<String> temp = new ArrayList<>();
                    temp.addAll(editions.get(yearTemp));
                    temp.add(semesterTemp);
                    editions.put(yearTemp, temp);
                    continue;
                }
                editions.put(yearTemp, Arrays.asList(semesterTemp));
            }
            Log.d(TAG, editions.toString());


            Map<String, Object> userData = new HashMap<>();
            userData.put(KEY_NAME, nameEdit.getText().toString());
            userData.put(KEY_ABRE, abreviationEdit.getText().toString());
            userData.put(KEY_ID, idEdit.getText().toString());
            userData.put(KEY_ECTS, ectsEdit.getText().toString());
            userData.put(KEY_EDITIONS, editions);
            Log.d(TAG, userData.toString());

            MainActivity.getDb().document("Degrees/" + MainActivity.getUserDegree().getID() + "/Courses/" + idEdit.getText().toString()).set(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ResponsibleNewCourse.this, "UC criada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResponsibleNewCourse.this, MainActivity.class);
                            createDocuments();
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "ERROR");
                        }
                    });
        }
    }

    private void createDocuments() {
        Map<String, Object> notEmpty = new HashMap<>();
        notEmpty.put("isEmpty", "false");

        for (String elem: ResponsibleNewEdition.getSelectedItems()){
            MainActivity.getDb().document("Degrees/" + MainActivity.getUserDegree().getID() + "/Courses/" + idEdit.getText().toString() + "/Editions/" + elem).set(notEmpty);

        }
    }
}
