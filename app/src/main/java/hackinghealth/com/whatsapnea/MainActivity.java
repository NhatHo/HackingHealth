package hackinghealth.com.whatsapnea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hackinghealth.com.whatsapnea.SQLiteDatabase.DBUtilities;

public class MainActivity extends AppCompatActivity {
    DBUtilities dbUtilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbUtilities = new DBUtilities(this);
    }

    public void CreateNewPatientProfile(View view) {
        Intent moveToPatientCreation = new Intent(view.getContext(), CreateProfileActivity.class);
        startActivityForResult(moveToPatientCreation, 0);
    }

    @Override
    protected void onDestroy() {
        dbUtilities.getmDbHelper().close();
        super.onDestroy();
    }
}
