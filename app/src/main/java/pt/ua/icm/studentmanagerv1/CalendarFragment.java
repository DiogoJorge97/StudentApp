package pt.ua.icm.studentmanagerv1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 2);

        List<EventDay> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.sample_icon));
//or
        events.add(new EventDay(calendar));



        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setEvents(events);

        calendarView.setOnDayClickListener( x ->
                Toast.makeText(CalendarFragment.super.getContext(), x.getCalendar().getTime().toString() + " ", Toast.LENGTH_SHORT).show()
        );

        /*CalendarPickerView datePicker = view.findViewById(R.id.calendarView);

        Date today = new Date();
        datePicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(today);*/


        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = null;
        try {
            date = sdf.parse("1970-01-01 00:00:00.000");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CalendarPickerView calendar = view.findViewById(R.id.calendar_view);


        calendar.init(date, nextYear.getTime(), new Locale("pt"));
        calendar.selectDate(new Date());*/

        /*datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);

                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);

                Toast.makeText(CalendarFragment.super.getContext(), selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });*/

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> {

            //getListOfCalendars();
            getCalendarEvents();

        });



        return view;
    }


    private void getListOfCalendars() {
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


    }

    private void getCalendarEvents() {
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

        if (cur != null) {
            cur.moveToFirst();

            //iterate over rows
            for (int i = 0; i < cur.getCount(); i++) {
                //iterate over the columns
                for (int j = 0; j < cur.getColumnNames().length; j++) {
                    stringBuilder2.append(cur.getColumnName(j) + "|");
                    if (cur.getColumnName(j).equals("title")) {
                        //append the column value to the string builder and delimit by a pipe symbol
                        stringBuilder.append(cur.getString(j) + "|");
                    }
                }
                //add a new line carriage return
                stringBuilder.append("\n");
                stringBuilder2.append("\n");


                //move to the next row
                cur.moveToNext();
            }
            //close the cursor
            cur.close();

            Toast.makeText(getActivity(), stringBuilder, Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(getActivity(), "Nothing to show", Toast.LENGTH_SHORT).show();

        }

    }



}
