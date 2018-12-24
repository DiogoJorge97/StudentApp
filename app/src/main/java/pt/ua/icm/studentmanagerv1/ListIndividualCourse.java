package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ListIndividualCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_individual_course);

        Intent intent = getIntent();

        String hello = intent.getStringExtra(ListCoursesFragment.EXTRA_COURSE_SELECTION);
        Toast.makeText(this, hello, Toast.LENGTH_SHORT).show();
    }
}
