package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListCalculator extends AppCompatActivity {

    private static final String TAG = "DTag ListCalculator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calculator);

        Intent intent = getIntent();
        Map<String, List<String>> calculatorEvaluations = (Map<String, List<String>>)intent.getSerializableExtra(ListIndividualCourse.EXTRA_EVALUATIONS);
        Log.d(TAG, "Map: " + calculatorEvaluations.toString());
        showEvaluations(calculatorEvaluations);

//        ImageView main_lock = findViewById(R.id.main_lock);
        EditText main_grade = findViewById(R.id.main_grade);

        main_grade.setEnabled(false);

/*        main_lock.setOnClickListener(view1 -> {
            Log.d(TAG, main_grade.getKeyListener().toString());
            if (main_grade.isEnabled()){
                main_grade.setEnabled(false);
                main_lock.setImageResource(R.drawable.ic_lock);
            } else {
                main_grade.setEnabled(true);
                main_lock.setImageResource(R.drawable.ic_unlock);

            }


        });*/



    }

    private void showEvaluations(Map<String,List<String>> calculatorEvaluations) {
        LinearLayout parent = findViewById(R.id.list_all_calculator);
        EditText main_grade = findViewById(R.id.main_grade);

        String name = "";
        String percentage = "";
        String grade = "";

        List<EditText> editTextList = new ArrayList<>();

        for (Map.Entry entry: calculatorEvaluations.entrySet()){

            LayoutInflater factory = LayoutInflater.from(this);
            View view = factory.inflate(R.layout.list_individual_calculator, null);

            TextView nameTV = view.findViewById(R.id.calc_name);
            TextView percentageTV = view.findViewById(R.id.calc_percentage);
            EditText gradeEV = view.findViewById(R.id.calc_grade);
            editTextList.add(gradeEV);
            ImageView unLockIV = view.findViewById(R.id.calc_un_lock);


            name = entry.getKey().toString();
            List<String> values = new ArrayList<>();
            values = (List<String>) entry.getValue();
            percentage = values.get(0);
            grade = values.get(1);

            nameTV.setText(name);
            percentageTV.setText(percentage);
            if (!grade.equals("0")){
                gradeEV.setText(grade);
                gradeEV.setEnabled(false);
                unLockIV.setImageResource(R.drawable.ic_lock);
            }

            unLockIV.setOnClickListener(view1 -> {
                int isEnabled = 0;



                Log.d(TAG, gradeEV.getKeyListener().toString());
                if (gradeEV.isEnabled()){
                    gradeEV.setEnabled(false);
                    unLockIV.setImageResource(R.drawable.ic_lock);
                } else {
                    gradeEV.setEnabled(true);
                    unLockIV.setImageResource(R.drawable.ic_unlock);

                }

                for (EditText editText: editTextList){
                    if (editText.isEnabled()){
                        isEnabled++;
                    }
                }
                Log.d(TAG, "Is: Enable " + isEnabled);
                if (isEnabled!=1){
                    main_grade.setEnabled(false);
                } else {
                    main_grade.setEnabled(true);
                }


            });
            parent.addView(view);

        }

        main_grade.setEnabled(false);







    }
}
