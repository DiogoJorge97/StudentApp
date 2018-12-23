package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponsibleEditActivity extends AppCompatActivity {


    private static final String TAG = "DTag ResponsiblEditAct";
    ;
    TextView courseTextView;
    LinearLayout parent;
    LinearLayout subParent;

    Map<String, String> coursesList;
    List<String> itensOpened;
    List<String> itensCreated;
    List<String> editionID;
    List<Integer> tempCreated;


    Boolean subTabOpened;
    static final String EXTRA_SELECTED_EDITION = "Selected Edition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsible_edit_activiy);

        courseTextView = findViewById(R.id.textView_course);
        parent = findViewById(R.id.linear_view_courses);
        subParent = findViewById(R.id.layout_for_each_uc);

        editionID = new ArrayList<>();
        coursesList = new ArrayMap<>();
        itensOpened = new ArrayList<>();
        itensCreated = new ArrayList<>();
        tempCreated = new ArrayList<>();


        subTabOpened = false;
        findCourses();


    }


    private void findCourses() {
        AllMightyCreator.getDb().collection("/Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> data = document.getData();
                        String name = "";
                        String id = "";
                        for (Map.Entry<String, Object> entry : data.entrySet()) {

                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key.equals("Name")) {
                                name = value.toString();
                            } else if (key.equals("ID")) {
                                id = value.toString();
                            }
                        }
                        coursesList.put(id, name);

                    }
                    populateCourses();

                }).addOnFailureListener(e -> {
                    //TODO to do
                });
    }


    private void populateCourses() {
        for (Map.Entry<String, String> entry : coursesList.entrySet()) {
            View view = LayoutInflater.from(this).inflate(R.layout.edit_course_child, null);
            view.setId(Integer.parseInt(entry.getKey())); // course Id
            courseTextView = view.findViewById(R.id.textView_course);
            courseTextView.setText(entry.getValue());
            courseTextView.setTag("courseView:" + entry.getKey());
            parent.addView(view);


        }

    }

    public void onCourseClick(View view) {
        final String viewTag = view.findViewById(R.id.textView_course).getTag().toString();
        final String courseID = viewTag.split(":")[1];

        Log.d(TAG, itensOpened.toString());
        if (itensOpened.contains(courseID)) {
            /*for (Integer integer: tempCreated){
                itensOpened.remove(courseID);
                View v = findViewById(integer);
                Log.d(TAG, findViewById(Integer.parseInt(courseID)).toString());
                v.setVisibility(View.GONE);
            }*/

            //hide

        } else {
            itensOpened.add(courseID);
            if (itensCreated.contains("666" + courseID)) {
                for (Integer integer: tempCreated){
                    View v = findViewById(integer);
                    v.setVisibility(View.VISIBLE);
                }

            } else {
                Log.d(TAG, "Tag: " + viewTag);
                editionID.clear();

                AllMightyCreator.getDb().collection("/Degrees/" + AllMightyCreator.getUserDegree().getID() + "/Courses/" + courseID + "/Editions")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            editionID.add(document.getId());  //<Course id, edition id>
                        }
                        populateEditions(viewTag, courseID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO to do
                    }
                });
                itensCreated.add("666" + courseID);
            }
       }

    }

    private void populateEditions(String viewTag, String courseID) {
        for (String entry : editionID) {
            View view = LayoutInflater.from(this).inflate(R.layout.edit_courses_baby, null);
            TextView editionTextView = view.findViewById(R.id.edition_text_view);
            Button babyBtnNewActivity = view.findViewById(R.id.baby_btn_start);

            editionTextView.setText(entry);
            editionTextView.setTag("EditionView:" + viewTag.split(":")[1] + "/Editions/" + entry);
            babyBtnNewActivity.setTag(courseID + "/Editions/" + entry);
            String numb = courseID + editionID.indexOf(entry);
            String numbCr = "666" + courseID + editionID.indexOf(entry);
            tempCreated.add(Integer.parseInt(numbCr));
            babyBtnNewActivity.setId(Integer.parseInt(numb));
            Log.d(TAG, "BtnBaby: "+ babyBtnNewActivity.getId());
            subParent = findViewById(Integer.parseInt(courseID));
            String numb1 = "666" + courseID ;
            view.setId(Integer.parseInt(numb1));
            subParent.addView(view);


        }
    }

    public void onEditionClick(View view) {
        String btnTag = view.getTag().toString();
        Intent intent = new Intent(this, ResponsibleEditEvaluationsActivity.class);
        intent.putExtra(EXTRA_SELECTED_EDITION, btnTag);
        startActivity(intent);
    }







    /*public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
*/

}
