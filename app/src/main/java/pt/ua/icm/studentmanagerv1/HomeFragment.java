package pt.ua.icm.studentmanagerv1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class HomeFragment extends android.support.v4.app.Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference alovelaceDocumentRef = db.document("Students/Stundent80246/Courses/45424");


    String TAG = "MyTag";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }


    public void loadClasses(View view) {
        alovelaceDocumentRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Toast.makeText(getActivity(), "Document exists", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Document does not exists", Toast.LENGTH_SHORT).show();
                        }
                        ObjectClasses classes = documentSnapshot.toObject(ObjectClasses.class);
                        Log.d(TAG, "Abreviation: " + classes.getAbreviation());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


}
