package pt.ua.icm.studentmanagerv1;

import java.util.Map;

public class ObjectEvaluation {
    private String Name;
    private Map<String, Map<String, String>> PracticalEvaluation;

    public ObjectEvaluation() {
    }

    public ObjectEvaluation(String name, Map<String, Map<String, String>> practicalEvaluation) {
        Name = name;
        PracticalEvaluation = practicalEvaluation;
    }

    public String getName() {
        return Name;
    }

    public Map<String, Map<String, String>> getPracticalEvaluation() {
        return PracticalEvaluation;
    }

}
