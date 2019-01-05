package pt.ua.icm.studentmanagerv1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CalendarFragment extends android.support.v4.app.Fragment {

    private final String TAG = "DTag CalendarTest";

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;



    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };


    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final List<EventDay> events = new ArrayList<>();
    private static CalendarView calendarView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        /**Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 2);

        Calendar calendar = Calendar.getInstance();

        events.add(new EventDay(calendar, R.drawable.sample_icon));
//or
//        events.add(new EventDay(calendar));

        Log.w("TAG43", calendar.toString());

        calendarView = view.findViewById(R.id.calendarView);
        //calendarView.setEvents(events);

        //calendarView.setEvents(events);

        calendarView.setEnabled(true);
        Log.e("False or True", String.valueOf(calendarView.isEnabled()));
        calendarView.setOnDayClickListener( x ->
                Toast.makeText(CalendarFragment.super.getContext(), x.getCalendar().getTime().toString() + " ", Toast.LENGTH_SHORT).show()
        );

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> {

            //getListOfCalendars();
            getCalendarEvents();

        });**/

        return view;
    }

    /**private void getListOfCalendars() {
        // Run query
        Cursor cur = null;
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
                "hera@example.com"};
        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        cur = cr.query(uri, null, null, null, null);


        if (cur != null) {
            Log.d(TAG, "Has Calendars");
            cur.moveToFirst();
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);


            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder stringBuilder2 = new StringBuilder();
            //iterate over rows
            for (int i = 0; i < cur.getCount(); i++) {
                //iterate over the columns
                for (int j = 0; j < cur.getColumnNames().length; j++) {
                    stringBuilder2.append(cur.getColumnName(j) + "|");
                    //if (cur.getColumnName(j).equals("title")) {
                    //append the column value to the string builder and delimit by a pipe symbol
                    stringBuilder.append(cur.getString(j) + "|");


                }
                //add a new line carriage return
                stringBuilder.append("\n");
                stringBuilder2.append("\n");


                //move to the next row
                cur.moveToNext();
            }
            //close the cursor
            cur.close();

            Log.d(TAG, stringBuilder.toString());
            Log.d(TAG, stringBuilder2.toString());

            Toast.makeText(getActivity(), stringBuilder, Toast.LENGTH_LONG).show();


        }


    }**/

    /**private void getCalendarEvents() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CALENDAR)) {
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                //TODO second element of string was created by me, don't understand what it is supposed to be
            }
            Toast.makeText(getActivity(), "No permission", Toast.LENGTH_SHORT).show();
            return;
        }
        //cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        Cursor cur = null;
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"diogo.jorge997@gmail.com", "com.gmail",
                "diogo.jorge997@gmail.com"};
        // Submit the query and get a Cursor object back.
        //cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        cur = cr.query(uri, null, null, null, null);

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();

        if (cur != null) {
            cur.moveToFirst();

            //iterate over rows
            HashMap<String, ArrayList<String>> eventsAndDates = new HashMap<>();
            for (int i = 0; i < cur.getCount(); i++) {
                //iterate over the columns
                Long dtstart=null;
                Long dtend = null;
                String title="";
                for (int j = 0; j < cur.getColumnNames().length; j++) {
                    Boolean bool = cur.getColumnName(j).equals("dtend");
                    if (bool){
                        try {
                            dtend = Long.parseLong(cur.getString(j));
                        }catch (Exception e){}
                        stringBuilder2.append(cur.getString(j) + "|\n");
                    }

                    if (cur.getColumnName(j).equals("title")) {
                        //append the column value to the string builder and delimit by a pipe symbol
                        stringBuilder.append(cur.getString(j) + "|");
                        title = cur.getString(j);
                    }

                    if (cur.getColumnName(j).equals("dtstart")){
                        dtstart=Long.valueOf(cur.getString(j));
                    }

                    /**if (cur.getColumnName(j).equals("dtend")){
                        Log.e("TAG",cur.getString(j));
                    }**/
                }
                //add a new line carriage return
                /**stringBuilder.append("\n");
                stringBuilder2.append("\n");
                //Log.e("TAG",dtstart.toString());

                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(dtstart);
                Date d = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String date = sdf.format(d);
                //String date = DateFormat.format("dd-MM-yyyy", cal).toString();
                String date1;
                if (dtend == null) date1 = date;
                else{
                    //Log.w("In", "Entrei");
                    cal.setTimeInMillis(dtend);
                    Date b = cal.getTime();
                    date1 = sdf.format(b);
                    //date1 = DateFormat.format("dd-MM-yyyy", cal).toString();
                }

                stringBuilder3.append(date + "\n");

                ArrayList<String> dates = new ArrayList<>(); dates.add(date); dates.add(date1);
                eventsAndDates.put(title, dates);

                //move to the next row
                cur.moveToNext();
            }
            //close the cursor
            cur.close();
            StringBuilder stringBuilder4 = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> entry : eventsAndDates.entrySet()){
                String[] splitw = entry.getValue().get(0).split("-");
                for (String a : splitw) Log.e("TAG1", a);
                //Calendar c = new java.util.GregorianCalendar(Integer.parseInt(splitw[2]), Integer.parseInt(splitw[1]), Integer.parseInt(splitw[0]));
                //events.add(new EventDay(c, R.drawable.sample_icon));
                if (entry.getValue().get(0).equals(entry.getValue().get(1))) Log.w("OKOKOK", entry.getValue().toString());
                stringBuilder4.append(entry.getKey() + " - " + entry.getValue().toString() + "\n");
            }
            calendarView.setEvents(events);
            //Toast.makeText(getActivity(), stringBuilder, Toast.LENGTH_LONG).show();
            Log.e("SUB", stringBuilder2.toString());
            Toast.makeText(getActivity(), stringBuilder4, Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(getActivity(), "Nothing to show", Toast.LENGTH_SHORT).show();

        }

    }**/
