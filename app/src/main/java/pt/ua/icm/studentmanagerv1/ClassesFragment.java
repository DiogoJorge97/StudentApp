package pt.ua.icm.studentmanagerv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ClassesFragment extends android.support.v4.app.Fragment {


    DocumentReference alovelaceDocumentRef = MainActivity.getDb().document("Students/Stundent80246/Courses/45424"); //TODO make path dinamic


    String TAG = "MyTag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        Boolean hasCourses =  MainActivity.getHasCourses();
        Log.d(TAG, Boolean.toString(hasCourses));
        if (hasCourses){
            view = inflater.inflate(R.layout.fragment_classes, null);
        } else {
            view = inflater.inflate(R.layout.fragment_classes_setup, null);
        }

        Button button = view.findViewById(R.id.start_setup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSetUpActivity.class);
                startActivity(intent);
            }
        });


        loadClasses(view);
        return view;

    }


    public void loadClasses(View view) {
        alovelaceDocumentRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> hash = documentSnapshot.getData();
                            Log.d(TAG, "Test String: " + hash);
                            decriptHash(hash);
                        } else {
                            Toast.makeText(getActivity(), "Document does not exists", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to connect do DataBase", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @SuppressWarnings("unchecked")
    public void decriptHash(Map<String, Object> hash){
        String mAbbreviation;
        String mAcademicYear;
        String mSemesterName;
        String spaces;
        for ( Map.Entry<String, Object> entry : hash.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value.getClass()==HashMap.class){
                Log.d(TAG, "Test Object: "+ key);
                decriptHash((Map<String, Object>) value);
            }else {
                Log.d(TAG, "Test Object: "+ key + " - " + value );

            }

        }

    }


}
