package pt.ua.icm.studentmanagerv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddCourseActivity extends AppCompatActivity {

    Button mChosen;
    Button mAddButton;
    EditText nameEdit;
    EditText abreviationEdit;
    EditText idEdit;
    EditText ectsEdit;
    static final String KEY_NAME = "Name";
    static final String KEY_ABRE = "Abbreviation";
    static final String KEY_ECTS = "ECTS";
    static final String KEY_ID = "ID";

    CollectionReference allCoursesReference = MainActivity.getDb().collection("Degrees");
    private static final String TAG = "MyTag";
    private static final String TAG1 = "MyTag1";


    ArrayList<String[]> possibleItems;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);


        mAddButton = findViewById(R.id.add_courses);
        mChosen = findViewById(R.id.choseDegrees);
        nameEdit = findViewById(R.id.name_edit);
        abreviationEdit = findViewById(R.id.abr_edit);
        idEdit = findViewById(R.id.id_edit);
        ectsEdit = findViewById(R.id.ects_edit);


        possibleItems = new ArrayList<>();


        mAddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveToFirestore();
            }
        });


        MainActivity.getDb().collection("Degrees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            getAllCourses(task);
                            createArrayOfCourses(possibleItems);

                            checkedItems = new boolean[listItems.length];

                            allertDialog();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void allertDialog() {
        mChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddCourseActivity.this);
                mBuilder.setTitle("Escolha o/os curso/s");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                            }
                        } else if (mUserItems.contains(position)) {
                            mUserItems.remove((Integer) position);
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int nrOfChosen = mUserItems.size();
                        if (nrOfChosen != 1) {
                            String temp = nrOfChosen + " cursos selecionados";
                            mChosen.setText(temp);
                        } else {
                            String temp = nrOfChosen + " curso selecionado";
                            mChosen.setText(temp);

                        }
                    }
                });
                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }

        });

    }

    private String[] createArrayOfCourses(ArrayList<String[]> possibleItems) {
        listItems = new String[possibleItems.size()];
        int it = 0;
        for (String[] item : possibleItems) {
            listItems[it] = item[1];
            it++;
        }
        return listItems;
    }


    private void getAllCourses(@NonNull Task<QuerySnapshot> task) {
        for (QueryDocumentSnapshot document : task.getResult()) {
            String[] degreeData = new String[2];
            degreeData[0] = document.getId();
            degreeData[1] = retrieveNeededData(document.getData());
            //Log.d(TAG, Arrays.toString(degreeData));
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


    private void saveToFirestore() {
        final boolean[] flag = {false};
        String[] cursos = new String[mUserItems.size()];
        int iter = 0;
        for (int value : mUserItems) {
            cursos[iter] = possibleItems.get(value)[0];
            iter++;
        }
        Log.d(TAG1, Arrays.toString(cursos));

        Map<String, Object> userData = new HashMap<>();
        userData.put(KEY_NAME, nameEdit.getText().toString());
        userData.put(KEY_ABRE, abreviationEdit.getText().toString());
        userData.put(KEY_ID, ectsEdit.getText().toString());
        userData.put(KEY_ECTS, idEdit.getText().toString());

        for (String curso : cursos) {
            MainActivity.getDb().document("Degrees/"+ curso + "/Courses/"+idEdit.getText().toString()).set(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            flag[0] = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"ERROR");
                        }
                    });
        }

        if (flag[0] ==true){
            Toast.makeText(AddCourseActivity.this, "UC criada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
            //intent.putExtra("NMEC", nMec);
            startActivity(intent); //TODO será que o NMec tem que andar sempre a saltitar?

        }
    }
}
