package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListCoursesFragment extends android.support.v4.app.Fragment {

    //widgets
    ListView listView;
/*    List<String> coursesDocIdList;
    List<String> coursesNameList;*/
    Map<String, String> coursesNamesMap;
/*
    List<String> coursesNewName;
*/



    String TAG = "DTag";
    public static String EXTRA_COURSE_SELECTION = "ExtraCourseSelection";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        Boolean hasCourses = AllMightyCreator.getHasCourses();
        //Log.d(TAG, "HasCourses: " + Boolean.toString(hasCourses));
        if (!hasCourses) {
            view = inflater.inflate(R.layout.fragment_classes_setup, null);
            Button button = view.findViewById(R.id.start_setup);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ListEnroleCourses.class);
                startActivity(intent);
            });
            return view;
        } else {
            view = inflater.inflate(R.layout.list_courses_fragment, null);
        }

        listView = view.findViewById(R.id.courses_list_view);


        loadClasses();
        return view;
    }


    public void loadClasses() {
        AllMightyCreator.getDb().collection("Students/St" + AllMightyCreator.getnMec() + "/Courses").orderBy("documentId", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
/*                    coursesDocIdList = new ArrayList<>();
                    coursesNameList = new ArrayList<>();*/
                    coursesNamesMap = new HashMap<>();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> data = document.getData();
                        String documentId = "";
                        String name = "";
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            //Log.d(TAG, key);
                            //Log.d(TAG, value.toString());

                            if (key.equals("documentId")) {
                                //coursesDocIdList.add(value.toString());
                                documentId = value.toString();
                            }
                            if (key.equals("name")) {
                                //coursesNameList.add((value.toString()));
                                name = value.toString();
                            }
                        }
                        coursesNamesMap.put(name, documentId);
                    }
                    setListView();

                }).addOnFailureListener(e -> {

        });
    }


    private void setListView() {/*
        coursesNewName = new ArrayList<>();
        final List<String> stopSelect = new ArrayList<>();
        Log.d(TAG, "CourseNameList: " + coursesDocIdList.toString());
        String lastY = coursesDocIdList.get(0).split("#")[0];
        String lastS = coursesDocIdList.get(0).split("#")[1];
        coursesNewName.add(lastY);
        coursesNewName.add("   " + lastS);
        stopSelect.add(lastY);
        stopSelect.add("   " + lastS);

        for (String elem : coursesDocIdList) {
            String currentY = coursesDocIdList.get(coursesDocIdList.indexOf(elem)).split("#")[0];
            String currentS = coursesDocIdList.get(coursesDocIdList.indexOf(elem)).split("#")[1];
            String currentID = coursesDocIdList.get(coursesDocIdList.indexOf(elem)).split("#")[2];
            if (currentY.equals(lastY)) {
                if (currentS.equals(lastS)) {
                    lastY = currentY;
                    lastS = currentS;
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

        final List<Integer> positOfStop = new ArrayList<>();
        int counter = 0;
        int bigCounter = 0;
        for (String elem : coursesNewName) {
            if (stopSelect.contains(elem)) {
                positOfStop.add(coursesNewName.indexOf(elem));
            } else {
                coursesNewName.set(bigCounter, "      " + coursesNameList.get(counter));
                counter++;
            }
            bigCounter++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.rowlayout, R.id.Item, coursesNewName);
        listView.setAdapter(adapter);
        */

        List<String> nameList = new ArrayList<>();
        nameList.addAll(coursesNamesMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.rowlayout, R.id.Item, nameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedItem = ((TextView) view).getText().toString();
            selectedItem = coursesNamesMap.get(selectedItem);

            Intent intent = new Intent(getActivity(), ListIndividualCourse.class);
            intent.putExtra(EXTRA_COURSE_SELECTION, selectedItem);
            startActivity(intent);
        });


    }
}


