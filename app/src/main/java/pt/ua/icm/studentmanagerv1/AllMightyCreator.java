package pt.ua.icm.studentmanagerv1;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import objects.Degree;
import objects.EvaluationGroup;
import objects.Student;

public class AllMightyCreator {

    private static Degree userDegree;
    private static Student user;
    private static boolean hasCourses;
    private static String nmec;
    private static final String currentYear = "2018-2019";
    private static Map<String, EvaluationGroup> allEvaluationsMap;
    public static Map<String, Map<String, Object>> evaluationMap;

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String TAG = "DTag AllMightyCreator ";


    public AllMightyCreator(String nmec) {
        allEvaluationsMap = new HashMap<>();
        evaluationMap = new HashMap<>();
        this.nmec = nmec;
        createUserObjects();

    }

    public void createUserObjects() {
        Log.d(TAG, "NMec creating student: " + getnMec());
        getDb().collection("Students").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.getId().equals("St" + getnMec())) {
                            user = document.toObject(Student.class);
                            Log.d(TAG, user.toString());

                            createUserDegreeObject();
                            hasCourses();
                            getUserCourses();
                        }
                    }
                }).addOnFailureListener(e -> {
            //TODO To do
        });

    }

    private void getUserCourses() {
        getDb().collection("Students/St" + getUser().getNmec() + "/Courses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        createUserEvaluation(documentSnapshot.getId());
                        Log.d(TAG, "Doc name: " + documentSnapshot.getId());
                    }
                }).addOnFailureListener(e -> {

        });
    }

    public static void createUserEvaluation(String subPath) {
        if (allEvaluationsMap.get(subPath)!= null){
            allEvaluationsMap.remove(subPath);
        }
        getDb().collection("/Students/St" + getnMec() + "/Courses/" + subPath + "/Evaluations")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                EvaluationGroup evaluationGroup = documentSnapshot.toObject(EvaluationGroup.class);
                allEvaluationsMap.put(subPath, evaluationGroup);
                Log.d("DTag", "Evaluation Group: " + evaluationGroup.toString());


                Map<String, Map<String, Object>> practicalComponent = evaluationGroup.getPracticalComponent();
                Map<String, Map<String, Object>> theoreticalComponent = evaluationGroup.getTheoreticalComponent();
                Map<String, Map<String, Object>> theoreticalPracticalComponent = evaluationGroup.getTheoreticalPracticalComponent();
                Map<String, Map<String, Map<String, Object>>> componentType = new TreeMap<>();
                componentType.put("practicalComponent", practicalComponent);
                componentType.put("theoreticalComponent", theoreticalComponent);
                componentType.put("theoreticalPracticalComponent", theoreticalPracticalComponent);


                for (Map.Entry<String, Map<String, Map<String, Object>>> evaluationGroupMap : componentType.entrySet()) {
                    String evaluationGroupName = evaluationGroupMap.getKey();
                    Log.d("DTag", evaluationGroupMap.getKey());
                    for (Map.Entry<String, Map<String, Object>> evaluation : evaluationGroupMap.getValue().entrySet()) {


                        String name = "";
                        String date = "";
                        String bigDate = "";
                        String grade = "";
                        String percentage = "";
                        String timeStudied = "";
                        String componentTypeName = "";
                        String componentTypeAbr = "";


                        Map<String, Object> evaluationDataObject;
                        evaluationDataObject = evaluation.getValue();


                        for (Map.Entry<String, Object> specification : evaluationDataObject.entrySet()) {
                            String key = specification.getKey();
                            Object value = specification.getValue();
                            switch (key) {
                                case "Name":
                                    name = value.toString();
                                    break;
                            }
                        }

                        evaluationMap.put(subPath + "#" + evaluationGroupName +  "#" + name, evaluationDataObject);
                    }


                }
            }
        });
    }

    private void createUserDegreeObject() {
        getDb().collection("Degrees").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (user.getDegrees().contains(document.getId())) {
                            userDegree = document.toObject(Degree.class);
                            Log.d(TAG, userDegree.toString());
                        }

                    }
                }).addOnFailureListener(e -> {
            //TODO To do
        });
    }


    private void hasCourses() {
        getDb().collection("Students/St" + getUser().getNmec() + "/Courses").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    hasCourses = false;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        hasCourses = true;
                        Log.d(TAG, "Has Courses = True");
                    }
                }).addOnFailureListener(e -> {

        });
    }


    public static FirebaseFirestore getDb() {
        return db;
    }

    public static String getCurrentYear() {
        return currentYear;
    }

    public static String getnMec() {
        return nmec;
    }

    public static Boolean getHasCourses() {
        return hasCourses;
    }

    public static void setHasCourses(boolean hasCourses) {
        AllMightyCreator.hasCourses = hasCourses;
    }

    public static Student getUser() {
        return user;
    }

    public static Degree getUserDegree() {
        return userDegree;
    }

    public static Map<String, Map<String, Object>> getAllEvaluationsMap() {
        return evaluationMap;
    }
    public static EvaluationGroup getEvaluation(String subPath){
        return allEvaluationsMap.get(subPath);
    }

}
