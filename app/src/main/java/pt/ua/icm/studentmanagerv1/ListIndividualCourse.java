package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class ListIndividualCourse extends AppCompatActivity {

    private static final String TAG = "DTag ListIndividualCrs";
    ;
    String editionSubPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_individual_course);

        Intent intent = getIntent();

        editionSubPath = intent.getStringExtra(ListCoursesFragment.EXTRA_COURSE_SELECTION);
        Toast.makeText(this, editionSubPath, Toast.LENGTH_SHORT).show();

        getDirectorInfo();
        getEvaluationInfo();
    }

    private void getDirectorInfo() {
    }

    private void getEvaluationInfo() {
        //TODO forcing Continuous Evaluation
        AllMightyCreator.getDb().document("/Students/St" + AllMightyCreator.getnMec() + "/Courses/" + editionSubPath + "/Evaluations/Continuous Evaluation")
                .get().addOnSuccessListener(documentSnapshot -> {
            Log.d(TAG, documentSnapshot.getData().toString());
            ObjectEvaluationType objectEvaluation = documentSnapshot.toObject(ObjectEvaluationType.class);
            getEvaluationList(objectEvaluation);
        });


    }

    private void getEvaluationList(ObjectEvaluationType objectEvaluation) {
        LinearLayout parent = findViewById(R.id.list_all_evaluations);



        Log.d(TAG, objectEvaluation.toString());
        Map<String, Map<String, Object>> practicalComponent = objectEvaluation.getPracticalComponent();
        for (Map.Entry<String, Map<String, Object>> evaluation : practicalComponent.entrySet()) {

            String name = "";
            String date = "";
            String grade = "";
            String percentage = "";
            String timeStudied = "";

            Log.d(TAG, "KEY: " + evaluation.getKey());
            Log.d(TAG, "VALUES: " + evaluation.getValue().toString());
            Map<String, Object> evaluationData;
            evaluationData =  evaluation.getValue();
            for (Map.Entry<String, Object> specification: evaluationData.entrySet()){
                String key = specification.getKey();
                Object value = specification.getValue();
                switch (key){
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
                        date = sdf.format(date1);
                        break;
                    case "Name":
                        name = value.toString();
                        break;
                }
            }


            LayoutInflater factory = LayoutInflater.from(this);
            View view = factory.inflate(R.layout.list_individual_evaluations, null);

            TextView evName = view.findViewById(R.id.ev_name);
            TextView evDate = view.findViewById(R.id.ev_date);
            TextView evGrade = view.findViewById(R.id.ev_grade);
            TextView evTimeStudied = view.findViewById(R.id.ev_time_studied);
            TextView evPercentage = view.findViewById(R.id.ev_percentage);

            evName.setText(name);
            evDate.setText(date);
            evGrade.setText(grade);
            evTimeStudied.setText(timeStudied + "h");
            evPercentage.setText(percentage + "%");

            parent.addView(view);


        }
    }
}
