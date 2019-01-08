package pt.ua.icm.studentmanagerv1;

import android.Manifest;
import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
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

import com.google.firebase.firestore.DocumentSnapshot;

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

public class CalendarFragment {

    public void mainCalendar(Context context) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long startMillis = timestamp.getTime();

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        //context.startActivity(intent);

        AllMightyCreator.getDb().collection("Students/St" + AllMightyCreator.getnMec() + "/Courses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        getEvents(documentSnapshot.getId(), context);
                    }
                }).addOnFailureListener(e -> {

        });
    }

    public void getEvents(String courseEdition, Context context){
        AllMightyCreator.getDb().document("/Students/St" + AllMightyCreator.getnMec() + "/Courses/" + courseEdition + "/Evaluations/Discreet Evaluation").get()
                .addOnSuccessListener(documentSnapshot -> {

                    Map<String, Object> data = documentSnapshot.getData();
                    for (String key : data.keySet()) {
                        if (data.get(key) instanceof HashMap) {
                            Object camp = data.get(key);
                            for (Object key1 : ((HashMap) camp).values()) {
                                SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                                Date d1 = null;
                                try {
                                    d1 = sdf3.parse(String.valueOf(((HashMap) key1).get("Date")));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                HashMap ok = (HashMap) key1;
                                long beginTime = d1.getTime();
                                long endTime = d1.getTime();
                                String title = String.valueOf(((HashMap) key1).get("Name"));
                                insertEvent(1, beginTime, endTime, title, context);
                                /*for (Object x : ok.keySet()) {
                                    if (x.toString().equals("Study Sessions")) {
                                        HashMap dates = (HashMap) ok.getEvaluation(x);
                                        int cont = 0;
                                        for (Object y : dates.values()) {
                                            StringBuilder title1 = new StringBuilder(title + " - StudySessions ");
                                            ArrayList z = (ArrayList) y;
                                            Date d2 = null;
                                            try {
                                                d2 = sdf3.parse(String.valueOf(z.getEvaluation(0)));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            Date d3 = null;
                                            try {
                                                d3 = sdf3.parse(String.valueOf(z.getEvaluation(1)));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            long beginTimeSession = d2.getTime();
                                            long endTimeSession = d3.getTime();
                                            title1.append(cont);
                                            insertEvent(1, beginTimeSession, endTimeSession, title1.toString(), context);
                                            cont += 1;

                                        }
                                    }
                                }**/
                            }
                        }

                    }
                }).addOnFailureListener(e -> {
        });
    }


    public void insertEvent(long calID, long beginTime, long endTime, String title, Context context) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Portugal/Lisbon");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
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
