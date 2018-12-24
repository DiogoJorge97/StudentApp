package pt.ua.icm.studentmanagerv1;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectEvaluation {
    private String Name;
    private Map<String, Map<String, Object>> PracticalEvaluation; //O object pode ser String ou Date

    public ObjectEvaluation() {
    }

    public ObjectEvaluation(String name, Map<String, Map<String, Object>> practicalEvaluation) {
        Name = name;
        PracticalEvaluation = practicalEvaluation;
    }

    public String getName() {
        return Name;
    }

    public Map<String, Map<String, Object>> getPracticalEvaluation() {
        return PracticalEvaluation;
    }

    public void setAllGrades() {


        for (Map.Entry<String, Map<String, Object>> singleEv : PracticalEvaluation.entrySet()) {
            PracticalEvaluation.get(singleEv.getKey()).put("Grade", 0);
            PracticalEvaluation.get(singleEv.getKey()).put("Time Studied", 0);

        }
        //PracticalEvaluation.put(evName,temp);
    }

    @Override
    public String toString() {
        return "ObjectEvaluation{" +
                "Name='" + Name + '\'' +
                ", PracticalEvaluation=" + PracticalEvaluation +
                '}';
    }
}
