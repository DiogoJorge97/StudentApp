package pt.ua.icm.studentmanagerv1;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private static Intent intent;
    private static AllMightyCreator amc;
    private static String nmec;

    //Variables
    String[] CADEIRAS = {"Introdução à Engenharia de Software", "Complementos de Base de Dados", "Inteligencia Artificial", "Segurança Informática nas Organizações"};
    String[] ABREVIACOES = {"IES", "CBD", "IA", "SIO"};
    String[] AVALIACOES = {"Teste 1", "Teste 2", "Projeto", "Avaliação Continua"};
    String[] HORAS = {"9h-11h", "11h-13h", "14h-16h30"};
    String[] SALA = {"4.109", "4.208", "4.203"};
    int NR_AULAS = HORAS.length;
    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "DTag MainActivity";






    //----------------------------On Create Methods--------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bottom navigation bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());



/*
        Intent intent = getIntent();
        nmec = intent.getStringExtra(LoginActivity.EXTRA_NMEC);*/

        amc = new AllMightyCreator(nmec);

        //List of NextEvaluations and NextClasses
        //setListViews();
    }


    //-------------------------------------Getters---------------


    public static void setNmec(String nmec) {
        MainActivity.nmec = nmec;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //--------------------------Bottom Navigation and Option Menu Actions----------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_class:
                fragment = new ListCoursesFragment();
                break;
            case R.id.navigation_calendar:
                fragment = new CalendarFragment();
                break;
            case R.id.navigation_study:
                fragment = new StudyFragment();
                break;
            case R.id.navigation_boss:
                fragment = new ResponsibleFragment();
                break;
        }
        return loadFragment(fragment);
    }





    //-------------------------------Other shit-------------------------------


    /*private void setListViews() {
        ListView evaluationsList = findViewById(R.id.next_evaluation_list);
        ListView classesList = findViewById(R.id.next_classes_list);
        CustomAdapter1 customAdapter1 = new CustomAdapter1();
        CustomAdapter2 customAdapter2 = new CustomAdapter2();
        evaluationsList.setAdapter(customAdapter1);
        classesList.setAdapter(customAdapter2);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) evaluationsList.getLayoutParams();
        params.height = 160*AVALIACOES.length;                                                      //Height of each element in list
        evaluationsList.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) classesList.getLayoutParams();
        params2.height = 120*HORAS.length;                                                      //Height of each element in list
        classesList.setLayoutParams(params2);
    }




    class CustomAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            return CADEIRAS.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position <= 4) {                                                                    //TODO BD - Alterar
                convertView = getLayoutInflater().inflate(R.layout.next_evaluation, null);
                TextView cadeiraTV = convertView.findViewById(R.id.cadeira_textView);
                TextView avaliacaoTV = convertView.findViewById(R.id.avaliacao_textView);

                cadeiraTV.setText(ABREVIACOES[position]);
                avaliacaoTV.setText(AVALIACOES[position]);
                return convertView;

            }
            convertView = getLayoutInflater().inflate(R.layout.empty_layout, null);             //TODO BD - Delete
            return convertView;
        }
    }


    class CustomAdapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return CADEIRAS.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            if (position < NR_AULAS){                                                               //TODO BD - Alterar
                convertView = getLayoutInflater().inflate(R.layout.next_classes, null);
                TextView timeTV = convertView.findViewById(R.id.time_TV);
                TextView classTV = convertView.findViewById(R.id.classTV);
                TextView roomTV = convertView.findViewById(R.id.roomTV);

                timeTV.setText(HORAS[position]);
                classTV.setText(AVALIACOES[position]);
                roomTV.setText(SALA[position]);
                return convertView;
            }
            convertView = getLayoutInflater().inflate(R.layout.empty_layout, null);            //TODO BD - Delete
            return convertView;
        }
    }

*/
}
