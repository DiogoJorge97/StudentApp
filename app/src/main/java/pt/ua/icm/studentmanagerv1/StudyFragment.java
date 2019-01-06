package pt.ua.icm.studentmanagerv1;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import list.ListEnroleCourses;
import list.ListIndividualCourse;
import responsible.ResponsibleEditEvaluationsActivity;

public class StudyFragment extends android.support.v4.app.Fragment {

    Map<String, String> courseIDAbr;
    Button button;
    private Calendar mCalendar;
    Button day1;
    Button day2;
    Button hour1;
    Button hour2;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat hourFormat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        courseIDAbr = new HashMap<>();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        hourFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());


        View view = inflater.inflate(R.layout.fragment_study, null);
        createDegress(view);
        return view;
    }

    private void populateView(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.study_linear);

        Map<String, Map<String, Object>> evaluationList = AllMightyCreator.getAllEvaluationsMap();

        for (Map.Entry entry : evaluationList.entrySet()) {
            String name = entry.getKey().toString();
            String ucId = name.split("#")[2];
            String eName = name.split("#")[3];

            String timeStudied = "";
            Map<String, Object> eval = (Map<String, Object>) entry.getValue();
            for (Map.Entry<String, Object> entry1 : eval.entrySet()) {
                String key = entry1.getKey();
                Object value = entry1.getValue();
                if (key.equals("Time Studied")) {
                    timeStudied = value.toString();
                }
            }
            LayoutInflater inflater1 = LayoutInflater.from(getContext());
            View view1 = inflater1.inflate(R.layout.study_individual_evaluation, null);

            TextView hours = view1.findViewById(R.id.hours_tv);
            TextView uc = view1.findViewById(R.id.uc_tv);
            TextView eNameTV = view1.findViewById(R.id.e_name_tv);
            Button studyButton = view1.findViewById(R.id.study_btn);
            Log.d("DTag","Button: " + (studyButton==null));
            Log.d("DTag", "ToString: " + courseIDAbr.toString());
            hours.setText(timeStudied);
            uc.setText(courseIDAbr.get(ucId));
            eNameTV.setText(eName);

            studyButton.setOnClickListener(view2 -> studyDialog());

            linearLayout.addView(view1);
        }
    }

    private void studyDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_study_session, null);

        day1 = mView.findViewById(R.id.day_1);
        day2 = mView.findViewById(R.id.day_2);
        hour1 = mView.findViewById(R.id.hora_1);
        hour2 = mView.findViewById(R.id.hora_2);

        day1.setOnClickListener(view -> {
            mCalendar = Calendar.getInstance();
            new DatePickerDialog(getActivity(), mDateDataSet, mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        day2.setOnClickListener(view -> {
            mCalendar = Calendar.getInstance();
            new DatePickerDialog(getActivity(), mDateDataSet1, mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        hour1.setOnClickListener(view -> {
            new TimePickerDialog(getActivity(), mTimeDataSet, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true).show();
        });

        hour2.setOnClickListener(view -> {
            new TimePickerDialog(getActivity(), mTimeDataSet1, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true).show();
        });


        mBuilder.setPositiveButton(R.string.guardar, (dialog, id) -> {


        }).setNegativeButton(R.string.cancelar, (dialog, id) -> {
            // User cancelled the dialog
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void createDegress(View view) {
        AllMightyCreator.getDb().collection("Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                String id = document.getId();
                Log.d("DTag", "Id:: " + id);
                String abr = "";
                Map<String, Object> data = document.getData();
                for (Map.Entry entry : data.entrySet()) {
                    if (entry.getKey().equals("Abbreviation")) {
                        abr = entry.getValue().toString();
                    }
                }
                courseIDAbr.put(id, abr);
            }
            populateView(view);
        });
    }


    /* After user decided on a date, store those in our calendar variable and then start the TimePickerDialog immediately */
    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            day1.setText(dateFormat.format(mCalendar.getTime()));
        }
    };

    /* After user decided on a time, save them into our calendar instance, and now parse what our calendar has into the TextView */
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            hour1.setText(hourFormat.format(mCalendar.getTime()));
        }
    };

    /* After user decided on a date, store those in our calendar variable and then start the TimePickerDialog immediately */
    private final DatePickerDialog.OnDateSetListener mDateDataSet1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            day2.setText(dateFormat.format(mCalendar.getTime()));
        }
    };

    /* After user decided on a time, save them into our calendar instance, and now parse what our calendar has into the TextView */
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            hour2.setText(hourFormat.format(mCalendar.getTime()));
        }
    };



}
