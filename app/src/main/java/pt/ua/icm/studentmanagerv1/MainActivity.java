package pt.ua.icm.studentmanagerv1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    //Variables
    String[] CADEIRAS = {"Introdução à Engenharia de Software", "Complementos de Base de Dados", "Inteligencia Artificial", "Segurança Informática nas Organizações"};
    String[] ABREVIACOES = {"IES", "CBD", "IA", "SIO"};
    String[] AVALIACOES = {"Teste 1", "Teste 2", "Projeto", "Avaliação Continua"};
    String[] HORAS = {"9h-11h", "11h-13h", "14h-16h30"};
    String[] SALA = {"4.109", "4.208", "4.203"};
    int NR_AULAS = HORAS.length;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_class:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_calendar:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_study:
                    mTextMessage.setText(R.string.title_home);
                    return true;
            }
            return false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bottom navigation bar
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //List of NextEvaluations and NextClasses
        setListViews();


    }

    private void setListViews() {
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

}
