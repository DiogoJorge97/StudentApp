package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListCoursesFragment extends android.support.v4.app.Fragment {

    //widgets
    ListView listView;
    List<String> coursesDocIdList;
    List<String> coursesNameList;
    Map<String, List<String>> coursesNamesMap;
    List<String> coursesNewName;

    String TAG = "DTag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        Boolean hasCourses = MainActivity.getHasCourses();
        Log.d(TAG, "HasCourses: " + Boolean.toString(hasCourses));
        if (!hasCourses) {
            view = inflater.inflate(R.layout.fragment_classes_setup, null);
            Button button = view.findViewById(R.id.start_setup);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListEnroleCourses.class);
                    startActivity(intent);
                }
            });
            return view;
        } else {
            view = inflater.inflate(R.layout.list_courses_fragment, null);
        }

        listView = view.findViewById(R.id.courses_list_view);


        loadClasses();
        return view;

    }


    private void setListView() {
        coursesNewName = new ArrayList<>();
        final List<String> stopSelect = new ArrayList<>();
        Log.d(TAG, "CourseNameList: " + coursesDocIdList.toString());
        String lastY = coursesDocIdList.get(0).split("#")[0];
        String lastS = coursesDocIdList.get(0).split("#")[1];
        coursesNewName.add(lastY);
        coursesNewName.add("   " + lastS);
        stopSelect.add(lastY);
        stopSelect.add("   " + lastS);

        for (String elem: coursesDocIdList){
            String currentY = coursesDocIdList.get(coursesDocIdList.indexOf(elem)).split("#")[0];
            String currentS = coursesDocIdList.get(coursesDocIdList.indexOf(elem)).split("#")[1];
            String currentID = coursesDocIdList.get(coursesDocIdList.indexOf(elem)).split("#")[2];
            if (currentY.equals(lastY)){
                if (currentS.equals(lastS)){
                    lastY=currentY;
                    lastS=currentS;
                    coursesNewName.add(elem);
                    continue;
                }
                coursesNewName.add(currentY + "   " + currentS);
                stopSelect.add(currentY + "   " + currentS);
                coursesNewName.add(elem);
                lastY = currentY;
                lastS = currentS;
                continue;
            }
            stopSelect.add(currentY + "   " + currentS);
            coursesNewName.add(currentY + "   " + currentS);
            coursesNewName.add(elem);
            lastY = currentY;
            lastS = currentS;
        }

        Log.d(TAG, coursesNewName.toString());
        Log.d(TAG, stopSelect.toString());



        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.rowlayout, R.id.Item, coursesNewName);
        listView.setAdapter(adapter);

        final List<Integer> positOfStop = new ArrayList<>();
        int counter = 0;
        int bigCounter = 0;
        for (String elem: coursesNewName){
            if (stopSelect.contains(elem)){
                positOfStop.add(coursesNewName.indexOf(elem));
            } else{
                coursesNewName.set(bigCounter, "      " + coursesNameList.get(counter));
                counter++;
            }
            bigCounter++;
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = ((TextView) view).getText().toString();
                for(Integer it : positOfStop) {
                    listView.getChildAt(it).setEnabled(false);
                    listView.getChildAt(it).setClickable(false);
                }
            }
        });


    }


    public void loadClasses() {
        MainActivity.getDb().collection("Students/St" + MainActivity.getnMec() + "/Courses").orderBy("documentId", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        coursesDocIdList = new ArrayList<>();
                        coursesNameList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Map<String, Object> data = document.getData();
                            for (Map.Entry<String, Object> entry : data.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                Log.d("MyTag4", key);
                                if (key.equals("documentId")) {
                                    coursesDocIdList.add(value.toString());
                                }
                                if (key.equals("name")){
                                    coursesNameList.add((value.toString()));
                                }
                            }
                        }
                        setListView();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }





}


