package pt.ua.icm.studentmanagerv1;

import android.Manifest;
import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarFragment extends Fragment {

    public static final String ACCOUNT_NAME = "Local Calendar";

    public static final String ACCOUNT_TYPE = "ua.pt.myapplication.account";

    private static final String INT_NAME_PREFIX = "local_";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        createCalendar("testes", 0xffff0000);
        createCalendar("estudo", 0x0000ffff);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long startMillis = timestamp.getTime();

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);

        String today = "07/01/2019";

        String tomorrow = "08/01/2019";

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = formatter.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date1 = null;
        try {
            date1 = formatter.parse(tomorrow);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        AllMightyCreator.getDb().document("/Students/St" + AllMightyCreator.getnMec() +"/Courses/2018-2019#Primeiro#45424/Evaluations/Discreet Evaluation").get()
                .addOnSuccessListener(documentSnapshot -> {

                    Map<String, Object> data = documentSnapshot.getData();
                    for ( String key : data.keySet() ) {
                        if (data.get(key) instanceof HashMap){
                            Object camp = data.get(key);
                            for (Object key1 : ((HashMap) camp).values()){
                                SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                                Date d1 = null;
                                try{
                                    d1 = sdf3.parse(String.valueOf(((HashMap) key1).get("Date")));

                                }catch (Exception e){ e.printStackTrace(); }
                                HashMap ok = (HashMap) key1;
                                long beginTime = d1.getTime();
                                long endTime  = d1.getTime();
                                String title = String.valueOf(((HashMap) key1).get("Name"));
                                insertEvent(1, beginTime, endTime, title, "testes");
                                for (Object x : ok.keySet()){
                                    if (x.toString().equals("Study Sessions")){
                                        HashMap dates = (HashMap) ok.get(x);
                                        for (Object y : dates.values()){
                                            int cont = 0;
                                            for (Object z : (ArrayList) y){
                                                StringBuilder title1 = new StringBuilder(title + " - StudySessions ");
                                                Date d2 = null;
                                                try{
                                                    d2 = sdf3.parse(String.valueOf(z));

                                                }catch (Exception e){ e.printStackTrace(); }
                                                long beginTimeSession = d2.getTime();
                                                long endTimeSession  = d2.getTime();
                                                title1.append(cont);
                                                insertEvent(1, beginTimeSession, endTimeSession, title1.toString(), "estudo");
                                                cont+=1;
                                            }

                                        }
                                    }
                                }
                            }
                        }

                    }
                }).addOnFailureListener(e -> {
        });

        /*for (Map.Entry<String, HashMap<String, Object>> val : infos.entrySet()){
            Log.w("WARNING", val.getKey());
        }*/

        return null;
    }

    private void createCalendar(String displayName, int color) {
        Uri uri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE).build();

        String intName = INT_NAME_PREFIX + displayName;
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE);
        cv.put(CalendarContract.Calendars.NAME, intName);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, displayName);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, color);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        ContentResolver cr = getActivity().getContentResolver();
        cr.insert(uri, cv);

        /**Intent main = new Intent();
        main.putExtra("useurl", resultUri);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(resultUri);
        startActivity(intent);**/
    }

    public void insertEvent(long calID, long beginTime, long endTime, String title, String nameCalendar) {
        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Portugal/Lisbon");
        //values.put(CalendarContract.Calendars.NAME, nameCalendar);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/events"), null, null, null, null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex("Title")).equals(title)){
                    exists = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        if (!exists) {
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }

    }

}
