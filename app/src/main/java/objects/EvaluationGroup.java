package objects;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import list.ListIndividualCourse;
import pt.ua.icm.studentmanagerv1.R;

public class EvaluationGroup {
    private String name;
    private Map<String, Map<String, Object>> practicalComponent; //O object pode ser String ou Date
    private Map<String, Map<String, Object>> theoreticalComponent;
    private Map<String, Map<String, Object>> theoreticalPracticalComponent;


    public EvaluationGroup() {
    }

    public EvaluationGroup(String name, Map<String, Map<String, Object>> practicalComponent, Map<String, Map<String, Object>> theoreticalComponent, Map<String, Map<String, Object>> theoreticalPracticalComponent) {
        this.name = name;
        this.practicalComponent = practicalComponent;
        this.theoreticalComponent = theoreticalComponent;
        this.theoreticalPracticalComponent = theoreticalPracticalComponent;
    }

    public String getName() {
        return name;
    }

    public Map<String, Map<String, Object>> getPracticalComponent() {
        return practicalComponent;
    }

    public Map<String, Map<String, Object>> getTheoreticalComponent() {
        return theoreticalComponent;
    }

    public Map<String, Map<String, Object>> getTheoreticalPracticalComponent() {
        return theoreticalPracticalComponent;
    }




    public void setAllGradesToDefault() {
        for (Map.Entry<String, Map<String, Object>> singleEv : practicalComponent.entrySet()) {
            practicalComponent.get(singleEv.getKey()).put("Grade", 0);
            practicalComponent.get(singleEv.getKey()).put("Time Studied", 0);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm", Locale.getDefault());
            Map<String, List<Date>> studySession;
            studySession = new HashMap<>();
            studySession.put("19/12/2018 19:22:00", new ArrayList<>());
            try {
                studySession.get("19/12/2018 19:22:00").add(formatter.parse("19/12/2018 19:22:00"));
                studySession.get("19/12/2018 19:22:00").add(formatter.parse("19/12/2018 20:37:00"));

            } catch (ParseException e) {
                Log.d("DTag", "Failed to add Dates");
            }
            practicalComponent.get(singleEv.getKey()).put("Study Sessions", studySession);

        }
        for (Map.Entry<String, Map<String, Object>> singleEv : theoreticalComponent.entrySet()) {
            theoreticalComponent.get(singleEv.getKey()).put("Grade", 0);
            theoreticalComponent.get(singleEv.getKey()).put("Time Studied", 0);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm", Locale.getDefault());
            Map<String, List<Date>> studySession;
            studySession = new HashMap<>();
            studySession.put("19/12/2018 19:22:00", new ArrayList<>());
            try {
                studySession.get("19/12/2018 19:22:00").add(formatter.parse("19/12/2018 19:22:00"));
                studySession.get("19/12/2018 19:22:00").add(formatter.parse("19/12/2018 20:37:00"));

            } catch (ParseException e) {
                Log.d("DTag", "Failed to add Dates");
            }
            theoreticalComponent.get(singleEv.getKey()).put("Study Sessions", studySession);

        }
        for (Map.Entry<String, Map<String, Object>> singleEv : theoreticalPracticalComponent.entrySet()) {

            theoreticalPracticalComponent.get(singleEv.getKey()).put("Grade", 0);
            theoreticalPracticalComponent.get(singleEv.getKey()).put("Time Studied", 0);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm", Locale.getDefault());
            Map<String, List<Date>> studySession;
            studySession = new HashMap<>();
            studySession.put("19/12/2018 19:22:00", new ArrayList<>());
            try {
                studySession.get("19/12/2018 19:22:00").add(formatter.parse("19/12/2018 19:22:00"));
                studySession.get("19/12/2018 19:22:00").add(formatter.parse("19/12/2018 20:37:00"));

            } catch (ParseException e) {
                Log.d("DTag", "Failed to add Dates");
            }
            theoreticalPracticalComponent.get(singleEv.getKey()).put("Study Sessions", studySession);

        }
    }

    public void getSingleEvaluation(String evName) {

    }

    @Override
    public String toString() {
        return "EvaluationGroup{" +
                "name='" + name + '\'' +
                ", practicalComponent=" + practicalComponent +
                ", theoreticalComponent=" + theoreticalComponent +
                ", theoreticalPracticalComponent=" + theoreticalPracticalComponent +
                '}';
    }
}
