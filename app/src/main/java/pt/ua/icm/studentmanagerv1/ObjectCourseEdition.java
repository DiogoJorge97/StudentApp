package pt.ua.icm.studentmanagerv1;

import java.util.List;
import java.util.Map;

public class ObjectCourseEdition {

    private Map<String, Object> AlternativeEvaluation;
    private Map<String, Object> DiscreetEvaluation;
    private Map<String, Object> FinalEvaluation;  //<FinalEvaluation, ComponentP>
    private List<String> Director;
    private String isEmpty;

    private Map<String, Map> ComponentP; //<Pratical, Evaluation>
    private Map<String, Map> ComponentT; //<Theoretical, Evaluation>
    private Map<String, Map> ComponentTP; //<TheoreticalPractical, Evaluation>

    private Map<String, Map> Evaluation; //<Evaluation, EvaluationSpec>
    private Map<String, String> EvaluationSpec; //<Percentagem, 50>

    //---Final,-P;T;TP----Eval----SpecNa--Spec
    //Map<String, List<Map<String, <String, String>>>>

    public ObjectCourseEdition() {
    }

    public ObjectCourseEdition(Map<String, Object> AlternativeEvaluation, Map<String, Object> DiscreetEvaluation, Map<String, Object> FinalEvaluation, List<String> Director, String isEmpty) {
        this.AlternativeEvaluation = AlternativeEvaluation;
        this.DiscreetEvaluation = DiscreetEvaluation;
        this.FinalEvaluation = FinalEvaluation;
        this.Director = Director;
        this.isEmpty = isEmpty;
    }


    public Map<String, Object> getAlternativeEvaluation() {
        return AlternativeEvaluation;
    }

    public Map<String, Object> getDiscreetEvaluation() {
        return DiscreetEvaluation;
    }

    public Map<String, Object> getFinalEvaluation() {
        return FinalEvaluation;
    }

    public List<String> getDirector() {
        return Director;
    }

    @Override
    public String toString() {
        return "ObjectCourseEdition{" +
                "AlternativeEvaluation=" + AlternativeEvaluation +
                ", DiscreetEvaluation=" + DiscreetEvaluation +
                ", FinalEvaluation=" + FinalEvaluation +
                ", Director=" + Director +
                '}';
    }
}
