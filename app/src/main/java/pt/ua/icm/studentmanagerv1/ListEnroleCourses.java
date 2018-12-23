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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListEnroleCourses extends AppCompatActivity {


    private List<String> coursesNames;
    private ListView listView;
    private ArrayList<String> selectedItems;
    Button nextStep;

    private final String TAG = "DTag ListEnroleCourses";
    static final String EXTRA_COURSES = "ExtraCourses";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        coursesNames = new ArrayList<>();
        selectedItems = new ArrayList<>();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_enrole_courses);


        loadAvailableCourses();

        nextStep = findViewById(R.id.next_step_btn);
        nextStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveChanges();
            }
        });

    }


    //TODO Dinamic Degree
    private void loadAvailableCourses() {
        Log.d(TAG, "Curso: " + AllMightyCreator.getUserDegree().getID());
        AllMightyCreator.getDb().collection("Degrees/" + AllMightyCreator.getUserDegree().getID() +"/Courses").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Log.d(TAG,"Course: " + document.getData().toString());
                ObjectCourse course = document.toObject(ObjectCourse.class);
                coursesNames.add(course.getName());
            }
            setListView();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO complete
            }
        });
    }

    private void setListView() {
        listView = findViewById(R.id.checkable_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout_check, R.id.checkItem, coursesNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem);
                } else {
                    selectedItems.add(selectedItem);
                }
            }
        });
    }

    private void saveChanges() {
        Intent intent = new Intent(this, ListEnroleCourses2.class);
        intent.putExtra(EXTRA_COURSES,selectedItems);
        startActivity(intent);
    }
}
