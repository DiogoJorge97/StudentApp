package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ResponsibleNewEdition extends AppCompatActivity {

    //widgets
    private Button mSaveBtn;
    private Button mCancelBtn;
    private ListView listView;
    private static ArrayList<String> selectedItems;
    ArrayList<String> allEditionsNames;


    ArrayList<String> listOfAvalilabreYears;

    private static final String TAG = "DTag DialogNewEdition";
    static final String EXTRA_EDITIONS = "ExtraEditions";


    public static ArrayList<String> getSelectedItems() {
        return selectedItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsible_new_edition);

        mSaveBtn = findViewById(R.id.new_courses_save_btn);
        mCancelBtn = findViewById(R.id.new_courses_cancel_btn);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResponsibleNewEdition.this, ResponsibleNewCourse.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResponsibleNewEdition.this, ResponsibleNewCourse.class);
                Collections.sort(selectedItems);
                for (String elm: selectedItems){
                    int i = selectedItems.indexOf(elm);
                    elm = elm.replaceAll(" Semestre", "");
                    selectedItems.set(i,elm);
                }
                Log.d(TAG, selectedItems.toString());
                intent.putExtra(EXTRA_EDITIONS, selectedItems);
                startActivity(intent);
                finish();



            }
        });

        selectedItems = new ArrayList<>();
        listOfAvalilabreYears = new ArrayList<>();
        listOfAvalilabreYears.addAll(MainActivity.getUserDegree().getDirectors().keySet());
        Collections.sort(listOfAvalilabreYears);
        String[] semesterArray = {"Primeiro Semestre", "Segundo Semestre", "Semestre Especial"};


        allEditionsNames = new ArrayList<>();


        for (String elem : listOfAvalilabreYears) {
            for (int i = 0; i < semesterArray.length; i++) {
                String nS = elem + " " + semesterArray[i];
                allEditionsNames.add(nS);
            }
        }


        //createListView();
        setListView();
    }


    private void setListView() {
        listView = findViewById(R.id.new_courses_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout_check, R.id.checkItem, allEditionsNames);
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


}
