package pt.ua.icm.studentmanagerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ResponsibleDiscreeteFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "DTag RespDiscreeteFrag";

    LinearLayout parent;

    private EditText directorName;
    private Button saveDirectorName;
    private EditText directorEmail;
    private Button saveDirectorEmail;
    private EditText editName;
    private Button saveEditName;
    private TextView displayDate;
    private Button editDate;
    private EditText percentage;
    private Button savePercentage;
    private Spinner spinnerSeason;
    private Spinner spinnerComponent;
    private Button saveTest;

    private ObjectCourseEdition edition;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discreete, container, false);

        parent = view.findViewById(R.id.discreete_parent);


        createEditionObject();

        return view;
    }

    private void createEditionObject() {
        Log.d(TAG, "Degrees/" + MainActivity.getUserDegree().getID() + "/Courses/" + ResponsibleEditEvaluationsActivity.getSubPath());
        MainActivity.getDb().document("Degrees/" + MainActivity.getUserDegree().getID() + "/Courses/" + ResponsibleEditEvaluationsActivity.getSubPath())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.e(TAG, documentSnapshot.getData().toString());

                edition = documentSnapshot.toObject(ObjectCourseEdition.class);
/*
                Map<String, Object> discreatEvaluation =
                Map<String, Object> practical = (Map<String, Object>) documentSnapshot.get("Pratical"); //pratical{evaluation=evData, evaluation2=ev2Data}
                Set<String> practicalKeys = practical.keySet();
                for(String key: practicalKeys){
                    Map<String, String> evaluationP = (Map<String, String>) documentSnapshot.get(key); //evaluation{Name=Project, Percentage=50}
                }

                Map<String, Object> theoretical = (Map<String, Object>) documentSnapshot.get("Theoretical");//theoretical{evaluation=evData, evaluation2=ev2Data}
                Set<String> theoreticalKeys = theoretical.keySet();
                for(String key: theoreticalKeys){
                    Map<String, String> evaluationT = (Map<String, String>) documentSnapshot.get(key); //evaluation{Name=Project, Percentage=50}
                }

                Map<String, Object> theoreticalPractical = (Map<String, Object>) documentSnapshot.get("TheoreticalPractical");//theoreticalPractical{evaluation=evData, evaluation2=ev2Data}
                Set<String> theoreticalPracticallKeys = theoreticalPractical.keySet();
                for(String key: theoreticalPracticallKeys){
                    Map<String, String> evaluationTP = (Map<String, String>) documentSnapshot.get(key); //evaluation{Name=Project, Percentage=50}
                }*/



                Log.d(TAG,"Edition" + edition.toString());
                //createEvViews(evaluationList);
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void createEvViews(Map<String, Object> evaluationList){

        for (Map.Entry<String, Object> entry : evaluationList.entrySet()) {
            String key = entry.getKey();
            if (key.equals("MinimalScore")){continue;}
            Object value = entry.getValue();
            Log.e(TAG, "Key: " + key + " Values:" + value);



            final LayoutInflater  inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.add_new_evaluation, null);

            editName = v.findViewById(R.id.edit_name);
            saveEditName = v.findViewById(R.id.save_edit_name);
            displayDate = v.findViewById(R.id.date);
            editDate = v.findViewById(R.id.edit_date);
            percentage = v.findViewById(R.id.percentage);
            savePercentage = v.findViewById(R.id.save_percentage);
            saveTest = v.findViewById(R.id.button_save_test);

            //editDate.setText();


            parent.addView(v);


        }
    }
}




