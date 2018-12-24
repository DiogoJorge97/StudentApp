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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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



    private SimpleDateFormat mSimpleDateFormat;



    private static final String TAG = "DTag RespEditEvalAct";

    static String subPath;
    private Calendar mCalendar;

    TextView mDateTextView;

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
        Button  dateButton = mView.findViewById(R.id.date_btn);
        mDateTextView = mView.findViewById(R.id.date_text_view);
        dateButton.setOnClickListener(textListener);

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
            mDateTextView.setText(mSimpleDateFormat.format(mCalendar.getTime()));
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

        Map<String,Object> evaluation = new HashMap<>();
        evaluation.put(name, evaluationData);

        Map<String,Object> practicalEvaluation = new HashMap<>();
        //TODO forcing practical evaluation
        practicalEvaluation.put("PracticalEvaluation", evaluation);


        //TODO Forcing continuous Evaluation
        //TODO Change to use ObjectEvaluation
        AllMightyCreator.getDb().document("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + subPath + "/Evaluations/Continuous Evaluation")
                .set(practicalEvaluation, SetOptions.merge()).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Yupi", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                });

    }


    public static String getSubPath() {
        return subPath;
    }
}
