package pt.ua.icm.studentmanagerv1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.core.Tag;

import java.util.Calendar;

public class CalendarTest extends AppCompatActivity {

    private final String TAG = "DTag CalendarTest";

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    Button button;
    Cursor cursor;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListOfCalendars();
                //getCalendarEvents();
            }
        });
    }

    private void getListOfCalendars() {
        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
                "hera@example.com"};
        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

            Toast.makeText(CalendarTest.this, stringBuilder, Toast.LENGTH_LONG).show();


        }


    }

    private void getCalendarEvents() {
        if (ActivityCompat.checkSelfPermission(CalendarTest.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CalendarTest.this,
                    Manifest.permission.READ_CALENDAR)) {
                Log.d("MYTAG", "Expl");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                Log.d("MYTAG", "No expl needed");

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(CalendarTest.this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                //TODO second element of string was created by me, don't understand what it is supposed to be
            }
            Log.d("MYTAG", "No permission");

            Toast.makeText(CalendarTest.this, "No permission", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("MYTAG", "Log working");

        //cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
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

            Toast.makeText(CalendarTest.this, stringBuilder, Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(CalendarTest.this, "Nothing to show", Toast.LENGTH_SHORT).show();

        }

    }


}
