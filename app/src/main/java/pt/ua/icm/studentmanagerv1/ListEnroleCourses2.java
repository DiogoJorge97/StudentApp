package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEnroleCourses2 extends AppCompatActivity {


    private ArrayList<String> selectedItems;
    private LinearLayout mLinear;
    private Map<String, String> valuesToDB;
    private Button save;
    private Map<String, String> directorData;

    private final String TAG = "DTag ListEnroleCourses2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_enrole_courses2);

        selectedItems = new ArrayList<>();
        mLinear = findViewById(R.id.linearL);
        save = findViewById(R.id.button10);
        valuesToDB = new HashMap<>();

        Intent intent = getIntent();
        selectedItems = intent.getStringArrayListExtra(ListEnroleCourses.EXTRA_COURSES);

        Log.d(TAG, "Selected Items: " + selectedItems.toString());
        directorData = new HashMap<>();

        loadAvailableCourses();

        save.setOnClickListener(view -> saveToFirebase());

    }


    //TODO Dinamic Degree
    private void loadAvailableCourses() {
        AllMightyCreator.getDb().collection("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ObjectCourse course = document.toObject(ObjectCourse.class);
                if (selectedItems.contains(course.getName())) {
                    chooseAcademicInfo(course);
                }
                // show line, wait for end of user edit to proceed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO complete
            }
        });
    }

    private void chooseAcademicInfo(final ObjectCourse course) {
        android.view.View v = getLayoutInflater().inflate(R.layout.enrole_courses_single, null);
        TextView textView = v.findViewById(R.id.textView14);
        Spinner spinner = v.findViewById(R.id.spinner1);
        final Spinner spinner2 = v.findViewById(R.id.spinner2);


        textView.setText(course.getAbbreviation());
        String[] keyList = course.getEditions().keySet().toArray(new String[course.getEditions().keySet().size()]);

        Log.d(TAG, "KeyList: " + Arrays.toString(keyList));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, keyList);
        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();

        final List<String> semesters = new ArrayList<>();
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, semesters);
        spinner2.setAdapter(spinnerArrayAdapter2);
        spinner.setSelection(0);
        spinner2.setSelection(0);


        mLinear.addView(v);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                String year = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "Year: " + year);
                semesters.clear();
                chooseSemester(course, year, spinner2, spinnerArrayAdapter2, semesters);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


    }

    private void chooseSemester(final ObjectCourse course, final String year, Spinner spinner2, ArrayAdapter<String> spinnerArrayAdapter2, List<String> semesters) {
        String[] tempArray = course.getEditions().get(year).toArray(new String[course.getEditions().get(year).size()]);

        for (String el : tempArray) {
            semesters.add(el);
        }
        spinnerArrayAdapter2.notifyDataSetChanged();

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                String semester = adapterView.getItemAtPosition(i).toString();
                String editionId = year + "#" + semester;
                createFinalData(course.getID(), editionId);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void createFinalData(String id, String editionId) {
        if (valuesToDB.containsKey(id)) {
            valuesToDB.remove(id);
        }
        valuesToDB.put(id, editionId);
    }

    private void saveToFirebase() {
        for (Map.Entry elem : valuesToDB.entrySet()) {
            final String id = elem.getKey().toString();
            final String edition = elem.getValue().toString();

            AllMightyCreator.getDb().collection("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses")
                    .whereEqualTo("ID", id)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ObjectCourse courseTemp = document.toObject(ObjectCourse.class);
                            saveStudentSubscription(id, edition, courseTemp);

                        }
                        AllMightyCreator.setHasCourses(true);
                    }).addOnFailureListener(e -> {
                //TODO complete
            });

            AllMightyCreator.getDb().document("/Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + id + "/Editions/" + edition.split("#")[0] + " " + edition.split("#")[1])
                    .get().addOnSuccessListener(documentSnapshot -> {
                for (Map.Entry<String, Object> entry : documentSnapshot.getData().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if (key.equals("E-mail")) {
                        directorData.put(key, value);
                    } else if (key.equals("Name")) {
                        directorData.put(key, value);
                    }

                }
                saveDirectorData(edition, id, directorData);

            }).addOnFailureListener(e -> {
            });


            //TODO Continuous Evaluation forced
            String cont = "Discreet Evaluation";
            Log.d(TAG, "/Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + id + "/Editions/" + edition.split("#")[0] + " " + edition.split("#")[1] + "/Evaluations/");
            AllMightyCreator.getDb().collection("/Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + id + "/Editions/" + edition.split("#")[0] + " " + edition.split("#")[1] + "/Evaluations/")
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.getId().equals(cont)) {
                        Log.d(TAG, "WORKING");
                        Log.d(TAG, "Snapshot: " + documentSnapshot.getData().toString());
                        ObjectEvaluationType objectEvaluation = documentSnapshot.toObject(ObjectEvaluationType.class);
                        saveStudentEdition(objectEvaluation, cont, edition, id);
                    }
                }

            }).addOnFailureListener(e -> {
                Log.d(TAG, "Damn Fail");
            });


        }


        Log.d(TAG, "VTDB: " + valuesToDB.toString());
    }

    private void saveDirectorData(String edition, String id, Map<String, String> directorData) {
        AllMightyCreator.getDb().document("Students/St" + AllMightyCreator.getnMec() + "/Courses/" + edition + "#" + id).set(directorData, SetOptions.merge());
    }

    private void saveStudentEdition(ObjectEvaluationType objectEvaluation, String evaluationType, String edition, String id) {
        Log.d(TAG, "Object: " + objectEvaluation.toString());
        objectEvaluation.setAllGradesToDefault();
        AllMightyCreator.getDb().document("Students/St" + AllMightyCreator.getnMec() + "/Courses/" + edition + "#" + id + "/Evaluations/" + evaluationType)
                .set(objectEvaluation)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "YES: " + objectEvaluation.toString());

                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "WHY?");

                });
    }

    private void saveStudentSubscription(String id, String edition, ObjectCourse course) {
        course.setEmptyEditions();
        course.setDocumentId(edition + "#" + id);
        AllMightyCreator.getDb().document("Students/St" + AllMightyCreator.getnMec() + "/Courses/" + edition + "#" + id).set(course, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Aluno inscrito");
                }).addOnFailureListener(e -> {
            Log.d(TAG, "Aluno não inscrito");
        });

        Intent intent = new Intent(ListEnroleCourses2.this, MainActivity.class);
        startActivity(intent);
    }

}
