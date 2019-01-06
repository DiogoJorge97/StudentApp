package responsible;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pt.ua.icm.studentmanagerv1.R;

public class ResponsibleFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comission,
                container, false);
        Button buttonAdd = view.findViewById(R.id.add_classes);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResponsibleNewCourse.class);
                startActivity(intent);
            }
        });

        Button buttonEdit = view.findViewById(R.id.edit_evaluation);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResponsibleEditActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
