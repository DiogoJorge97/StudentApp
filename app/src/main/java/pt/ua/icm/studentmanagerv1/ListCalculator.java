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

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        Map<String, List<String>> calculatorEvaluations = (Map<String, List<String>>) intent.getSerializableExtra(ListIndividualCourse.EXTRA_EVALUATIONS);
        Log.d(TAG, "Map: " + calculatorEvaluations.toString());

        isEnabled = 4;
        main_lock = findViewById(R.id.main_lock);
        main_grade = findViewById(R.id.main_grade);

        showEvaluations(calculatorEvaluations);

        main_grade.setEnabled(false);
        isEnabled--;

        main_lock.setOnClickListener(view1 -> {
            if (main_grade.isEnabled()) {
                main_grade.setEnabled(false);
                isEnabled--;
                main_lock.setImageResource(R.drawable.ic_lock);
            } else {
                main_grade.setEnabled(true);
                main_lock.setImageResource(R.drawable.ic_unlock);
                isEnabled++;

            }
        });


    }

    private void showEvaluations(Map<String, List<String>> calculatorEvaluations) {
        LinearLayout parent = findViewById(R.id.list_all_calculator);
        editTextList = new HashMap<>();

        String name;
        String percentage;
        String grade;


        for (Map.Entry entry : calculatorEvaluations.entrySet()) {

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

            editTextList.put(gradeEV, percentage.replace("%", ""));


            nameTV.setText(name);
            percentageTV.setText(percentage);
            if (!grade.equals("0")) {
                gradeEV.setText(grade);
                gradeEV.setEnabled(false);
                isEnabled--;
                unLockIV.setImageResource(R.drawable.ic_lock);
            }

            editTextList.put(main_grade, "100");
            unLockIV.setOnClickListener(view1 -> {

                if (gradeEV.isEnabled()) {
                    gradeEV.setEnabled(false);
                    isEnabled--;
                    unLockIV.setImageResource(R.drawable.ic_lock);
                } else {
                    gradeEV.setEnabled(true);
                    isEnabled++;
                    unLockIV.setImageResource(R.drawable.ic_unlock);
                }
            });
            parent.addView(view);


            for (EditText editText : editTextList.keySet()) {
                editText.setOnEditorActionListener((textView, i, keyEvent) -> {
                    if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                        if (isEnabled == 2) {
                            calculateGrade(editText);
                        } else {
                            Log.d(TAG, "Enabled: " + isEnabled);
                            Toast.makeText(this, R.string.lock_error_message, Toast.LENGTH_LONG).show();
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
        for (Map.Entry editTextEntryMap : editTextList.entrySet()) {
            EditText editText = (EditText) editTextEntryMap.getKey();
            String grade = editText.getText().toString();
            String percentage = editTextEntryMap.getValue().toString();
            if (grade.isEmpty()) {
                grade = "0";
            }

            if (Double.parseDouble(percentage) == 100) {
                mainGrade = Double.parseDouble(grade);
                if (editText.isEnabled() && !editText.equals(pressedEditText)) {
                    varEditText = editText;
                    varPercentage = Double.parseDouble(percentage) / 100;
                }
                continue;
            }
            if (editText.isEnabled()) {
                if (editText.equals(pressedEditText)) {
                    fixedGrades.add(Double.parseDouble(grade) * Double.parseDouble(percentage) / 100);
                } else {
                    varGrade = Double.parseDouble(grade) * Double.parseDouble(percentage) / 100;
                    varEditText = editText;
                    varPercentage = Double.parseDouble(percentage) / 100;
                }
            } else {
                if (grade.isEmpty()) {
                    Toast.makeText(this, R.string.lock_error_message2, Toast.LENGTH_SHORT).show();
                    break;
                }
                fixedGrades.add(Double.parseDouble(grade) * Double.parseDouble(percentage) / 100);
            }


        }


        Double value;

        Double fixedTotal = 0.0;
        for (Double elem : fixedGrades) {
            fixedTotal += elem;
        }

        if (varPercentage == 1) {
            varGrade = mainGrade;
            varEditText = findViewById(R.id.main_grade);
            value = fixedTotal;
        } else {
            value = mainGrade - fixedTotal;
            value = value / varPercentage;
        }


        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        varEditText.setText(df.format(value));

        Log.d(TAG,"Fixed: "+fixedGrades.toString());
        Log.d(TAG,"Varia: "+varGrade);
        Log.d(TAG,"Main: "+mainGrade);


}


}
