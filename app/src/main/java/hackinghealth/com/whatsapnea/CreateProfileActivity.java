package hackinghealth.com.whatsapnea;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import hackinghealth.com.whatsapnea.SQLiteDatabase.DBUtilities;

/**
 * Created by paull on 2017-04-30.
 */

public class CreateProfileActivity extends Activity {
    private static String TAG = "CreateProfileActivity";
    private static EditText dateOfBirth;
    private static int bday=-1, bmonth=-1, byear=-1;
    private DBUtilities dbUtilities;
    private EditText fullname, history, allergies;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_creation_form);

        dbUtilities = new DBUtilities(this);

        Button saveProfile = (Button) findViewById(R.id.save_profile);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        dateOfBirth = (EditText) findViewById(R.id.date_of_birth);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dateofbirth");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                newFragment.show(ft, "dateofbirth");
            }
        });

        fullname = (EditText) findViewById(R.id.full_name);
        history = (EditText) findViewById(R.id.medical_history);
        allergies = (EditText) findViewById(R.id.allergies);

        Button savePatient = (Button) findViewById(R.id.save_profile) ;
        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullname.getText().toString();
                String historyInfo = history.getText().toString();
                String allergiesInfo = allergies.getText().toString();
                if (dbUtilities.addNewPatient(name, bday, bmonth, byear, historyInfo, allergiesInfo) > -1) {
                    Toast.makeText(v.getContext(), "Successfully store patient profile", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(v.getContext(), "Failed to store patient profile", Toast.LENGTH_LONG);
                }
                Intent moveToTakeVideo = new Intent(v.getContext(), TakeVideoActivity.class);
                startActivityForResult(moveToTakeVideo, 1);
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = byear;
            int day = bday;
            int month = bmonth;
            if (bday == -1 || bmonth == -1 || byear == -1) {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            bday = day;
            bmonth = month;
            byear = year;

            dateOfBirth.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(day).append("-").append(new DateFormatSymbols().getMonths()[month]).append("-").append(year));
        }
    }

    @Override
    protected void onDestroy() {
        dbUtilities.getmDbHelper().close();
        super.onDestroy();
    }
}


