package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AllMightyCreator {

    private static ObjectDegree userDegree;
    private static ObjectStudent user;
    private static boolean hasCourses;
    private static String nmec;
    private static final String currentYear = "2018-2019";


    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String TAG = "DTag AllMightyCreator ";




    public AllMightyCreator(String nmec) {

        this.nmec = nmec;
        createUserObjects();

    }

    public void createUserObjects() {
        Log.d(TAG, "NMec creating student: " + getnMec());
        getDb().collection("Students").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.getId().equals("St" + getnMec())){
                                user = document.toObject(ObjectStudent.class);
                                Log.d(TAG,user.toString());

                                createUserDegreeObject();

                                createUserCoursesObject();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO To do
            }
        });

    }

    private void createUserDegreeObject() {
        getDb().collection("Degrees").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (user.getDegrees().contains(document.getId())) {
                            userDegree = document.toObject(ObjectDegree.class);
                            Log.d(TAG, userDegree.toString());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO To do
            }
        });
    }

    private void createUserCoursesObject(){
        getDb().collection("Students/St" + getUser().getNmec() + "/Courses").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        hasCourses=false;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            hasCourses=true;
                            Log.d(TAG, "Has Courses = True");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
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

    public static ObjectStudent getUser() {
        return user;
    }

    public static ObjectDegree getUserDegree() {
        return userDegree;
    }

}
