package list;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import objects.EvaluationGroup;
import pt.ua.icm.studentmanagerv1.AllMightyCreator;
import pt.ua.icm.studentmanagerv1.R;

public class ListIndividualCourse extends AppCompatActivity {

    private static final String TAG = "DTag ListIndividualCrs";

    String nameCourse;
    String editionSubPath;
    private String directorEmail;
    private String directorName;
    private Map<String, List<String>> calculatorEvaluations;
    public static String EXTRA_EVALUATIONS = "EXTRA_EVALUATIONS";


    TextView directorEmailTV;
    TextView directorNameTV;
    ImageView directorInfo;
    ImageView messageAlert;

    TextView person;
    EditText namePerson;
    TextView situation;
    EditText textSituation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_individual_course);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ListIndividualCourse.this, ListCalculator.class);
            intent.putExtra(EXTRA_EVALUATIONS, (Serializable) calculatorEvaluations);
            startActivity(intent);
            finish();
        });

        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        editionSubPath = intent.getStringExtra(ListCoursesFragment.EXTRA_COURSE_SELECTION);
        nameCourse = intent.getStringExtra(ListCoursesFragment.EXTRA_COURSE_NAME);
        actionBar.setTitle(nameCourse);
        actionBar.show();
        Toast.makeText(this, editionSubPath, Toast.LENGTH_SHORT).show();

        directorInfo = findViewById(R.id.director_image);

       calculatorEvaluations = new HashMap<>();

       messageAlert = findViewById(R.id.imageView);


        directorInfo.setOnClickListener(view -> displaDirectorInfo());

        messageAlert.setOnClickListener(view -> displayCampMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getEvaluationInfo();
        getDirectorInfo();
        Log.d(TAG, "OnStart");
    }

    private void displayCampMessage() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListIndividualCourse.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_message_alert, null);

        EditText textAssunto = mView.findViewById(R.id.textAssunto);
        EditText textSituation = mView.findViewById(R.id.textSituation);

        mBuilder.setView(mView)
                .setPositiveButton("Enviar", (dialog, id) -> {
                    String [] addresses = {"catarinaxavier@ua.pt"};
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                    intent.putExtra(Intent.EXTRA_SUBJECT, textAssunto.getText().toString());
                    intent.putExtra(Intent.EXTRA_TEXT, textSituation.getText().toString());
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.ic_launcher_foreground));
                    try{

                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }


    public void displaDirectorInfo(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListIndividualCourse.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_director_data, null);


        directorEmailTV = mView.findViewById(R.id.director_email);
        directorNameTV = mView.findViewById(R.id.director_name);

        directorEmailTV.setText(directorEmail);
        directorNameTV.setText(directorName);


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    public void getDirectorInfo() {

        AllMightyCreator.getDb().document("Students/St" + AllMightyCreator.getnMec() + "/Courses/" + editionSubPath).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = documentSnapshot.getData();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue().toString();
                        if (key.equals("E-mail")) {
                            directorEmail = value;
                        } else if (key.equals("Name")) {
                            directorName = value;
                        }

                    }
                }).addOnFailureListener(e -> {

        });


    }

    private void getEvaluationInfo() {
        getEvaluationList(AllMightyCreator.getEvaluation(editionSubPath));
    }

    private void getEvaluationList(EvaluationGroup objectEvaluation) {
        LinearLayout parent = findViewById(R.id.list_all_evaluation);
        parent.removeAllViews();
        Boolean flag = false;


        Log.d(TAG, objectEvaluation.toString());
        Map<String, Map<String, Object>> practicalComponent = objectEvaluation.getPracticalComponent();
        Map<String, Map<String, Object>> theoreticalComponent = objectEvaluation.getTheoreticalComponent();
        Map<String, Map<String, Object>> theoreticalPracticalComponent = objectEvaluation.getTheoreticalPracticalComponent();
        Map<String, Map<String, Map<String, Object>>> componentType = new TreeMap<>();
        componentType.put("practicalComponent", practicalComponent);
        componentType.put("theoreticalComponent", theoreticalComponent);
        componentType.put("theoreticalPracticalComponent", theoreticalPracticalComponent);
        Log.d(TAG, "All maps: " + componentType.toString());


        for (Map.Entry<String, Map<String, Map<String, Object>>> evaluationGroup : componentType.entrySet()) {
            String evaluationGroupName = evaluationGroup.getKey();
            for (Map.Entry<String, Map<String, Object>> evaluation : evaluationGroup.getValue().entrySet()) {


                String name = ""; String date = ""; String bigDate = ""; String grade = ""; String percentage = ""; String timeStudied = ""; String componentTypeName = ""; String componentTypeAbr = "";


                Log.d(TAG, "KEY: " + evaluation.getKey());
                Log.d(TAG, "VALUES: " + evaluation.getValue().toString());
                Map<String, Object> evaluationData;
                evaluationData = evaluation.getValue();
                for (Map.Entry<String, Object> specification : evaluationData.entrySet()) {
                    String key = specification.getKey();
                    Object value = specification.getValue();
                    switch (key) {
                        case "Percentage":
                            percentage = value.toString();
                            break;
                        case "Grade":
                            grade = value.toString();
                            break;
                        case "Time Studied":
                            timeStudied = value.toString();
                            break;
                        case "Date":
                            Date date1 = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy kk:mm");
                            bigDate = sdf1.format(date1);
                            date = sdf.format(date1);
                            break;
                        case "Name":
                            name = value.toString();
                            break;
                        case "CpAbbreviation":
                            componentTypeAbr = value.toString();
                            break;
                        case "Component":
                            componentTypeName = value.toString();
                    }
                }


                LayoutInflater factory = LayoutInflater.from(this);
                View view = factory.inflate(R.layout.list_individual_evaluations, null);

                if (!flag) {
                    View divider = view.findViewById(R.id.divider);
                    divider.setVisibility(View.GONE);
                    flag = true;

                }

                TextView evName = view.findViewById(R.id.ev_name);
                TextView evDate = view.findViewById(R.id.ev_date);
                TextView evGrade = view.findViewById(R.id.ev_grade);
                TextView evTimeStudied = view.findViewById(R.id.ev_time_studied);
                TextView evPercentage = view.findViewById(R.id.ev_percentage);
                TextView evCompTypeAbr = view.findViewById(R.id.component_abbreviation);

                evName.setText(name);
                evDate.setText(date);

                List<String> subCalculator = new ArrayList<>();
                subCalculator.add(percentage +"%");

                if (grade.equals("0")) {
                    evGrade.setVisibility(View.GONE);
                    subCalculator.add("0");

                } else {
                    evGrade.setText(grade);
                    subCalculator.add(grade);

                }
                evTimeStudied.setText(timeStudied + "h");
                evPercentage.setText(percentage + "%");
                evCompTypeAbr.setText(componentTypeAbr);

                calculatorEvaluations.put(name, subCalculator);


                String finalName = name;
                String finalPercentage = percentage;
                String finalEvaluationGroupName = evaluationGroupName;
                String finalComponentTypeName = componentTypeName;
                String finalBigDate = bigDate;

                String finalGrade = grade;
                view.setOnClickListener(view1 -> {
                    Log.d(TAG, "Name: " + finalName);


                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListIndividualCourse.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_evaluation_data, null);

                    TextView nameSubTV = mView.findViewById(R.id.name_sub);
                    TextView componentSubTV = mView.findViewById(R.id.component_sub);
                    TextView percentageSubTV = mView.findViewById(R.id.percentage_sub);
                    TextView dateSubTV = mView.findViewById(R.id.date_sub);
                    EditText gradeSubEV = mView.findViewById(R.id.grade_sub);

                    if (!finalGrade.equals("0")){
                        gradeSubEV.setText(finalGrade);
                    }
                    nameSubTV.setText(finalName);
                    componentSubTV.setText(finalComponentTypeName);
                    percentageSubTV.setText(finalPercentage + "%");
                    dateSubTV.setText(finalBigDate);

                    mBuilder.setPositiveButton(R.string.guardar, (dialog, id) -> {
                        if (gradeSubEV.getText().toString().equals("0") || gradeSubEV.getText().toString().equals("")) {
                            evGrade.setVisibility(View.GONE);
                            saveGrade("0", finalName, evaluationGroupName);
                            gradeSubEV.setText("0");

                        } else if (Float.parseFloat(gradeSubEV.getText().toString()) <= 20 && Float.parseFloat(gradeSubEV.getText().toString()) > 0) {
                            saveGrade(gradeSubEV.getText().toString(), finalName, finalEvaluationGroupName);
                            evGrade.setVisibility(View.VISIBLE);
                            evGrade.setText(gradeSubEV.getText().toString());
                            gradeSubEV.setText(finalGrade);
                            AllMightyCreator.createUserEvaluation(editionSubPath);
                        } else {
                            Toast.makeText(getApplicationContext(), "Nota tem que estar entre 0 e 20\nNão guardado", Toast.LENGTH_SHORT).show();
                        }

                    }).setNegativeButton(R.string.cancelar, (dialog, id) -> {
                        // User cancelled the dialog
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                });

                parent.addView(view);

            }
        }
    }

    private void saveGrade(String grade, String evName, String evGroupName) {
        Log.d(TAG, evGroupName + "." + evName + ".Grade");

        AllMightyCreator.getDb().document("/Students/St" + AllMightyCreator.getnMec() + "/Courses/" + editionSubPath + "/Evaluations/Discreet Evaluation")
                .update(evGroupName + "." + evName + ".Grade", grade);
    }




}
