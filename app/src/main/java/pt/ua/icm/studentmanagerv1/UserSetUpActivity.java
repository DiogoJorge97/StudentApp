package pt.ua.icm.studentmanagerv1;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class UserSetUpActivity extends AppCompatActivity {

    Button mAddBtn;
    Button mChosenBtn;


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
        setContentView(R.layout.activity_user_set_up);

        mAddBtn = findViewById(R.id.add_courses);
        mChosenBtn = findViewById(R.id.choseDegrees);



        possibleItems = new ArrayList<>();


       /* mAddBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //chooseSemesters();
            }
        });*/


        MainActivity.getDb().document("Students/St" + MainActivity.getnMec())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document " + MainActivity.getnMec());
                            }
                            //getStudentDegrees(document.getData());
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void getStudentDegrees(Map<String, Object> data) {
        //String deggres;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Log.d(TAG, entry.toString());
        }



    }


    /*private void allertDialog() {
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
    */
}
