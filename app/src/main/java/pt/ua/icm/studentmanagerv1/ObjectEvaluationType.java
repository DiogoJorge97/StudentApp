package pt.ua.icm.studentmanagerv1;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ObjectEvaluationType {
    private String name;
    private Map<String, Map<String, Object>> practicalComponent; //O object pode ser String ou Date

    public ObjectEvaluationType() {
    }

    public ObjectEvaluationType(String name, Map<String, Map<String, Object>> practicalComponent) {
        this.name = name;
        this.practicalComponent = practicalComponent;
    }

    public String getName() {
        return name;
    }

    public Map<String, Map<String, Object>> getPracticalComponent() {
        return practicalComponent;
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
    }

    public void getSingleEvaluation(String evName){

    }

    @Override
    public String toString() {
        return "ObjectEvaluation{" +
                "name='" + name + '\'' +
                ", practicalComponent=" + practicalComponent +
                '}';
    }

}
