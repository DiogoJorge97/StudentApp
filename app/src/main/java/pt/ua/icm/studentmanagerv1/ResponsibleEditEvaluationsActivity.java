package pt.ua.icm.studentmanagerv1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ResponsibleEditEvaluationsActivity extends AppCompatActivity{

    private static Intent intent;

    private TextView directorNameTV;
    private Button editDirector;

    private String directorName;
    private String directorEmail;
    private String component;
    private String season;
    private Button dateButton;
    private String seasonName;

    Map<String, String> possibleComponents;


    private SimpleDateFormat mSimpleDateFormat;



    private static final String TAG = "DTag RespEditEvalAct";

    static String subPath;
    private Calendar mCalendar;
    private Spinner seasonSpinner;
    private Spinner componentSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsible_edit_evaluations);

        directorName = "";
        directorEmail="";



        Toolbar toolbar = findViewById(R.id.toolbar);

        directorNameTV = findViewById(R.id.director_name);
        editDirector = findViewById(R.id.director_edit_btn);



        setSupportActionBar(toolbar);
        intent = getIntent();
        subPath = intent.getStringExtra(ResponsibleEditActivity.EXTRA_SELECTED_EDITION);


        directorData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> createNewEval());

        editDirector.setOnClickListener(view -> {
            editDirectorDialog();
        });



    }





    private void directorData() {
        AllMightyCreator.getDb().document("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + subPath).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = documentSnapshot.getData();
                    for (Map.Entry<String, Object> entry : data.entrySet()){
                        String key = entry.getKey();
                        String value = entry.getValue().toString();
                        if (key.equals("E-mail")){
                            directorEmail = value;
                        } else if (key.equals("Name")) {
                            directorName = value;
                            directorNameTV.setText(directorName);
                        }

                    }
                }).addOnFailureListener(e -> {

                });

    }



    private void editDirectorDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ResponsibleEditEvaluationsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.edit_director, null);

        TextView directorNameEditTV = mView.findViewById(R.id.director_name_edit);
        TextView directorEmailEditTV = mView.findViewById(R.id.director_email_edit);

        directorNameEditTV.setText(directorName);
        directorEmailEditTV.setText(directorEmail);




        mBuilder.setPositiveButton("Guardar", (dialog, id) -> {
            directorName = directorNameEditTV.getText().toString();
            directorEmail = directorEmailEditTV.getText().toString();
            Map <String, String> directorData = new HashMap<>();
            directorData.put("Name", directorName);
            directorData.put("E-mail", directorEmail);
            directorNameTV.setText(directorName);

            AllMightyCreator.getDb().document("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + subPath)
                    .set(directorData);
        }).setNegativeButton("Cancelar", (dialog, id) -> {
            // User cancelled the dialog
        });
        // Create the AlertDialog object and return it
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void createNewEval() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ResponsibleEditEvaluationsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.new_evaluation, null);

        EditText mName = mView.findViewById(R.id.name_edit);
        EditText mPercentage = mView.findViewById(R.id.percentage_edit);
        dateButton = mView.findViewById(R.id.date_btn);
        seasonSpinner = mView.findViewById(R.id.season_spinner);
        componentSpinner = mView.findViewById(R.id.component_spinner);

        dateButton.setOnClickListener(textListener);
        componentSpinner();
        seasonSpinner();


        mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm", Locale.getDefault());



        mBuilder.setPositiveButton("Guardar", (dialog, id) -> {
            saveNewEvaluation(mName.getText().toString(), mPercentage.getText().toString(), mSimpleDateFormat.format(mCalendar.getTime()));
        }).setNegativeButton("Cancelar", (dialog, id) -> {
            // User cancelled the dialog
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();


    }

    /* Define the onClickListener, and start the DatePickerDialog with users current time */
    private final View.OnClickListener textListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCalendar = Calendar.getInstance();
            new DatePickerDialog(ResponsibleEditEvaluationsActivity.this, mDateDataSet, mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    /* After user decided on a date, store those in our calendar variable and then start the TimePickerDialog immediately */
    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(ResponsibleEditEvaluationsActivity.this, mTimeDataSet, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true).show();
        }
    };

    /* After user decided on a time, save them into our calendar instance, and now parse what our calendar has into the TextView */
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            dateButton.setText(mSimpleDateFormat.format(mCalendar.getTime()));
        }
    };

    private void saveNewEvaluation(String name, String percentage, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date ndate = null;
        try {
            ndate = formatter.parse(date);
        } catch (ParseException e) {
            Log.d(TAG,e.toString());
        }

        Map<String, Object> evaluationData = new HashMap<>();
        evaluationData.put("Date", ndate);
        evaluationData.put("Name",name);
        evaluationData.put("Percentage",percentage);

        Map<String, String> componentData = new HashMap<>();
        for (Map.Entry entry: possibleComponents.entrySet()) {
            if (entry.getValue().toString().equals(component)){
                String abbreviation = "";
                for (char ch: entry.getKey().toString().toCharArray()) {
                    if(Character.isUpperCase(ch)){
                        abbreviation += ch;
                    }
                }
                evaluationData.put("Component",abbreviation);
            }
        }





        Map<String,Object> evaluation = new HashMap<>();
        evaluation.put(name, evaluationData);
        Map<String,Object> fullEvaluation = new HashMap<>();
        Log.d(TAG, "COmponent before saved:" + component);
        fullEvaluation.put(component, evaluation);


        //TODO Forcing continuous Evaluation
        //TODO Change to use ObjectEvaluation
        AllMightyCreator.getDb().document("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + subPath + "/Evaluations/" + season)
                .set(fullEvaluation, SetOptions.merge()).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Yupi", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                });

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("name", seasonName);
        AllMightyCreator.getDb().document("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + subPath + "/Evaluations/" + season)
                .set(nameMap, SetOptions.merge());

    }


    private void componentSpinner() {

        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(this, R.array.component_array, R.layout.spinner_item);
        componentSpinner.setAdapter(spinnerArrayAdapter);

        possibleComponents = new HashMap<>();
        possibleComponents.put("Prática", "practicalComponent");
        possibleComponents.put("Teórica", "theoreticalComponent");
        possibleComponents.put("Teórica-Prática", "theoreticalPracticalComponent");


        component= possibleComponents.get("Prática");

        componentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String choosenComponent = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "choosen:" + choosenComponent);
                for (Map.Entry entry: possibleComponents.entrySet()) {
                    if (entry.getKey().equals(choosenComponent)) {
                        Log.d(TAG, "Component:" + component);
                        component = entry.getValue().toString();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void seasonSpinner() {

        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(this, R.array.season_array, R.layout.spinner_item);
        seasonSpinner.setAdapter(spinnerArrayAdapter);

        Map<String, String> possibleSeasons = new HashMap<>();
        possibleSeasons.put("Discreta", "Discreet Evaluation");
        possibleSeasons.put("Final", "Final Evaluation");
        possibleSeasons.put("Recurso", "Alternative Evaluation");

        season = possibleSeasons.get("Discreta");
        seasonName = "Discreta";

        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                String choosenSeason = parent.getItemAtPosition(position).toString();
                for (Map.Entry entry: possibleSeasons.entrySet()) {
                    if (entry.getKey().equals(choosenSeason)) {
                        season = entry.getValue().toString();
                        seasonName = entry.getKey().toString();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }




    public static String getSubPath() {
        return subPath;
    }
}
