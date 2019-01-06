package pt.ua.icm.studentmanagerv1;

import android.Manifest;
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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.calendar_fragment, container, false);
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
                                insertEvent(beginTime, endTime, title);
                            }
                        }

                    }
                }).addOnFailureListener(e -> {
            Log.w("WARNING", "ok");
        });

        /*for (Map.Entry<String, HashMap<String, Object>> val : infos.entrySet()){
            Log.w("WARNING", val.getKey());
        }*/

        return null;
    }

    public void insertEvent(long beginTime, long endTime, String title) {
        long calID = 1;
        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Portugal/Lisbon");
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
                Log.w("WARNING", cursor.getString(cursor.getColumnIndex("Title")) + "");
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
