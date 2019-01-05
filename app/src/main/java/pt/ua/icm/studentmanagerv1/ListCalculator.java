package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListCalculator extends AppCompatActivity {

    private static final String TAG = "DTag ListCalculator";
    private Map<EditText, String> editTextList;
    private EditText main_grade;
    private ImageView main_lock;
    private int isEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calculator);

        Intent intent = getIntent();
        Map<String, List<String>> calculatorEvaluations = (Map<String, List<String>>)intent.getSerializableExtra(ListIndividualCourse.EXTRA_EVALUATIONS);
        Log.d(TAG, "Map: " + calculatorEvaluations.toString());

        main_lock = findViewById(R.id.main_lock);
        main_grade = findViewById(R.id.main_grade);

        showEvaluations(calculatorEvaluations);

        main_grade.setEnabled(false);

        main_lock.setOnClickListener(view1 -> {
            Log.d(TAG, main_grade.getKeyListener().toString());
            if (main_grade.isEnabled()){
                main_grade.setEnabled(false);
                main_lock.setImageResource(R.drawable.ic_lock);
            } else {
                main_grade.setEnabled(true);
                main_lock.setImageResource(R.drawable.ic_unlock);

            }
        });





    }

    private void showEvaluations(Map<String,List<String>> calculatorEvaluations) {
        LinearLayout parent = findViewById(R.id.list_all_calculator);
        editTextList = new HashMap<>();

        String name = "";
        String percentage = "";
        String grade = "";


        for (Map.Entry entry: calculatorEvaluations.entrySet()){

            LayoutInflater factory = LayoutInflater.from(this);
            View view = factory.inflate(R.layout.list_individual_calculator, null);

            TextView nameTV = view.findViewById(R.id.calc_name);
            TextView percentageTV = view.findViewById(R.id.calc_percentage);
            EditText gradeEV = view.findViewById(R.id.calc_grade);
            gradeEV.setImeOptions(EditorInfo.IME_ACTION_DONE);
            ImageView unLockIV = view.findViewById(R.id.calc_un_lock);


            name = entry.getKey().toString();
            List<String> values;
            values = (List<String>) entry.getValue();
            percentage = values.get(0);
            grade = values.get(1);

            editTextList.put(gradeEV, percentage.replace("%",""));


            nameTV.setText(name);
            percentageTV.setText(percentage);
            if (!grade.equals("0")){
                gradeEV.setText(grade);
                gradeEV.setEnabled(false);
                unLockIV.setImageResource(R.drawable.ic_lock);
            }

            editTextList.put(main_grade, "100");
            unLockIV.setOnClickListener(view1 -> {
                isEnabled = 0;

                if (gradeEV.isEnabled()){
                    gradeEV.setEnabled(false);
                    unLockIV.setImageResource(R.drawable.ic_lock);
                } else {
                    gradeEV.setEnabled(true);
                    unLockIV.setImageResource(R.drawable.ic_unlock);
                }

                for (EditText editText: editTextList.keySet()){
                    if (editText.isEnabled()){
                        isEnabled++;
                    }
                }
/*                if (isEnabled!=1){
                    main_grade.setEnabled(false);
                } else {
                    main_grade.setEnabled(true);
                }*/


            });
            parent.addView(view);


            for (EditText editText: editTextList.keySet()){
                editText.setOnEditorActionListener((textView, i, keyEvent) -> {
                    Log.d(TAG, "I: " + i);
                    if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                        //Log.d(TAG, "" + keyEvent.getKeyCode());
                        if (isEnabled==2){
                            calculateGrade(editText);
                        } else {
                            Toast.makeText(this, "Desbloqueie apenas dois cadeados para se poder efectuar os calculos", Toast.LENGTH_LONG).show();
                        }
                    }
                    return false;
                });
            }
        }
        main_grade.setEnabled(false);







    }

    private void calculateGrade(EditText pressedEditText) {
        double varGrade = 0.0;
        EditText varEditText = null;
        double varPercentage = 100.0;
        double mainGrade = 0.0;
        List<Double> fixedGrades = new ArrayList<>();
        for (Map.Entry editTextEntryMap: editTextList.entrySet()){
            EditText editText = (EditText) editTextEntryMap.getKey();
            String editTxGrade = editText.getText().toString();
            if (editTxGrade.isEmpty()){
                editTxGrade = "0";
            }
            if (Double.parseDouble(editTextEntryMap.getValue().toString())==100){
                mainGrade = Double.parseDouble(editTxGrade);
                continue;
            }
            if (editText.isEnabled()){
                if (editText.equals(pressedEditText)){
                    fixedGrades.add(Double.parseDouble(editTxGrade) * Double.parseDouble(editTextEntryMap.getValue().toString())/100);
                } else {
                    varGrade = Double.parseDouble(editTxGrade) * Double.parseDouble(editTextEntryMap.getValue().toString())/100;
                    varEditText = editText;
                    varPercentage = Double.parseDouble(editTextEntryMap.getValue().toString())/100;
                }
            } else {
                fixedGrades.add(Double.parseDouble(editTxGrade) * Double.parseDouble(editTextEntryMap.getValue().toString())/100);
            }


        }


        if (varGrade == 0.0){
            varGrade = mainGrade;
            varEditText = findViewById(R.id.main_grade);
        }



        Log.d(TAG, "Fixed: " + fixedGrades.toString());
        Log.d(TAG, "Varia: " + varGrade);
        Log.d(TAG, "Main: " + mainGrade);

        Double fixedTotal= 0.0;
        for (Double elem: fixedGrades){
            fixedTotal += elem;
        }

        Double value = mainGrade - fixedTotal;
        value = value / varPercentage;
        Log.d(TAG, "VarEditID: " + varEditText.getId());
        varEditText.setText(value.toString());

    }



}
