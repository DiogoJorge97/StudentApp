package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class ResponsibleEditEvaluationsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static Intent intent;
    private static ObjectCourseEdition objectCourseEdition;

    private EditText directorName;
    private Button saveDirectorName;
    private EditText directorEmail;
    private Button saveDirectorEmail;
    private EditText editName;
    private Button saveEditName;
    private TextView displayDate;
    private Button editDate;
    private EditText percentage;
    private Button savePercentage;
    private Button saveTest;


    private FrameLayout parent;



    private static final String TAG = "DTag RespEditEvalAct";

    static String subPath;
    private Boolean stillEditing;
    private String baseID;
    private int createdNr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsible_edit_evaluations);



        //Bottom navigation bar
        BottomNavigationView navigationS = findViewById(R.id.navigationSeas);
        navigationS.setOnNavigationItemSelectedListener(this);
        //loadFragment(new ResponsibleDiscreeteFragment());


        Toolbar toolbar = findViewById(R.id.toolbar);
        parent = findViewById(R.id.parent_evaluations);

        directorName = findViewById(R.id.director_name_text);
        saveDirectorName = findViewById(R.id.director_name_btn);
        directorEmail = findViewById(R.id.director_email_text);
        saveDirectorEmail = findViewById(R.id.director_email_btn);

        setSupportActionBar(toolbar);
        stillEditing=false;
        createdNr=0;
        intent = getIntent();
        subPath = intent.getStringExtra(ResponsibleEditActivity.EXTRA_SELECTED_EDITION);

        createEditionObject();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stillEditing){
                    Toast.makeText( ResponsibleEditEvaluationsActivity.this,"Termine a edição", Toast.LENGTH_SHORT).show();
                }else{
                    stillEditing=true;
                    createNewEval();
                }
            }
        });
        Log.d(TAG, "SubPath: " + subPath);
    }

    public static ObjectCourseEdition getObjectCourseEdition() {
        return objectCourseEdition;
    }

    private void createEditionObject() {
        MainActivity.getDb().document("Degrees/" + MainActivity.getUserDegree().getID() + "/Courses/" + subPath)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.e(TAG, documentSnapshot.getData().toString());

                objectCourseEdition = documentSnapshot.toObject(ObjectCourseEdition.class);
                populateDirector(objectCourseEdition);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void populateDirector(ObjectCourseEdition couseEdition) {
        Log.d(TAG, couseEdition.toString());
        if (couseEdition.getDirector()!=null){
            directorName.setText(objectCourseEdition.getDirector().get(0));
            directorEmail.setText(objectCourseEdition.getDirector().get(1));
            saveDirectorName.setText("Editar");
            saveDirectorEmail.setText("Editar");
            directorName.setEnabled(false);
            directorEmail.setEnabled(false);
        }else {
            Log.d(TAG, "No director");
        }
    }

    private void createNewEval() {
        baseID =  subPath.replaceAll("[^\\d.]", "");

        final LayoutInflater  inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.add_new_evaluation, null);

        editName = v.findViewById(R.id.edit_name);
        String s = "9" + baseID +  createdNr + "1";
        int i = Integer.parseInt("1234");
        editName.setId(Integer.parseInt("9" + baseID +  createdNr + "1"));
        saveEditName = v.findViewById(R.id.save_edit_name);
        saveEditName.setId(Integer.parseInt("9" + baseID +  createdNr + "2"));
        displayDate = v.findViewById(R.id.date);
        displayDate.setId(Integer.parseInt("9" + baseID +  createdNr + "3"));
        editDate = v.findViewById(R.id.edit_date);
        editDate.setId(Integer.parseInt("9" + baseID +  createdNr + "4"));
        percentage = v.findViewById(R.id.percentage);
        percentage.setId(Integer.parseInt("9" + baseID +  createdNr + "5"));
        savePercentage = v.findViewById(R.id.save_percentage);
        savePercentage.setId(Integer.parseInt("9" + baseID +  createdNr + "6"));
        saveTest = v.findViewById(R.id.button_save_test);
        saveTest.setId(Integer.parseInt("9" + baseID +  createdNr + "9"));


        parent.addView(v);

        createdNr++;
    }


    //--------------------------Bottom Navigation and Option Menu Actions----------------------

    private Boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            Log.d(TAG, "Fragment ID: " + fragment.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_evaluations, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_discreete:
                fragment = new ResponsibleDiscreeteFragment();
                Log.d(TAG, "Discrete MTF" + fragment.getId());
                break;
            case R.id.navigation_final:
                fragment = new ResponsibleFinalFragment();
                break;
            case R.id.navigation_alternative:
                fragment = new ResponsibleAlternativeFragment();
                break;
        }
        return loadFragment(fragment);
    }

    public static String getSubPath() {
        return subPath;
    }
}
