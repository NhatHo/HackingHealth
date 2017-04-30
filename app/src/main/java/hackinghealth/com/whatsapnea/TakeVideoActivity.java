package hackinghealth.com.whatsapnea;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import java.util.List;

import hackinghealth.com.whatsapnea.SQLiteDatabase.DBUtilities;

/**
 * Created by paull on 2017-04-30.
 */

public class TakeVideoActivity extends Activity {
    private static final String TAG = "TakeVideoActivity";
    private static final int ACTION_TAKE_VIDEO = 1;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final String VIDEO_STORAGE_KEY = "ViewVideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "VideoViewVisibility";
    private static final String WORSE = "Worse than regular";
    private static final String NORMAL = "Regular";
    private static final String BETTER = "Better than regular";
    private VideoView mVideoView;
    private Uri mVideoUri;
    private CameraUtilities cameraUtilities;
    private RelativeLayout mMainView;
    private PopupWindow mRatingSurvey;
    private String selectedStatus = NORMAL;
    private EditText extraNotes;
    DBUtilities dbUtilities;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_video_page);
        dbUtilities = new DBUtilities(this);

        mMainView = (RelativeLayout) findViewById(R.id.take_video_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, this);
        }
        cameraUtilities = new CameraUtilities();
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoUri = null;
        Button vidBtn = (Button) findViewById(R.id.startVideoNow);
        setBtnListenerOrDisable(
                vidBtn,
                mTakeVidOnClickListener,
                MediaStore.ACTION_VIDEO_CAPTURE
        );
    }
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
            mVideoUri = FileProvider.getUriForFile(
                    this, this.getApplicationContext().getPackageName() + ".provider",
            cameraUtilities.CreateVideoFile());
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoUri);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: " + e.getMessage());
        }
    }
    private void handleCameraVideo(Intent intent) {
        mVideoUri = intent.getData();
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.video_session_survey, null);
        mRatingSurvey = new PopupWindow(
                customView,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        if(Build.VERSION.SDK_INT >= 21){
            mRatingSurvey.setElevation(5.0f);
        }
        mRatingSurvey.showAtLocation(mMainView, Gravity.CENTER, 0, 0);

        RadioGroup radioGroup = (RadioGroup) customView.findViewById(R.id.rating_group);
        // This overrides the radiogroup onCheckListener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    switch (checkedRadioButton.getId()) {
                        case R.id.status_worse:
                            Log.d(TAG, "IT's WORSE");
                            selectedStatus = WORSE;
                            break;
                        case R.id.status_better:
                            Log.d(TAG, "it better");
                            selectedStatus = BETTER;
                            break;
                        case R.id.status_normal:
                        default:
                            Log.d(TAG, "it normal");
                            selectedStatus = NORMAL;
                            break;
                    }
                }
            }
        });

        extraNotes = (EditText)  customView.findViewById(R.id.survey_notes);

        Button saveButton = (Button) customView.findViewById(R.id.save_survey);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dbUtilities.addNewVideo(mVideoUri.toString(), selectedStatus, extraNotes.getText().toString());
                mRatingSurvey.dismiss();
            }
        });
    }

    Button.OnClickListener mTakeVidOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakeVideoIntent();
                }
            };
    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application’s environment.
     * @param action The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_VIDEO: {
                if (resultCode == RESULT_OK) {
                    handleCameraVideo(data);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Video recording cancelled.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Failed to record video",
                            Toast.LENGTH_LONG).show();
                }
                break;
            } // ACTION_TAKE_VIDEO
        } // switch
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.setVisibility(
                savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }
    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {
        String[] PERMISSIONS = {Manifest.permission.CAMERA
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        }
    }
    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Handle if needed
                }
                break;
            }default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            // other ‘case’ lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onDestroy() {
        dbUtilities.getmDbHelper().close();
        super.onDestroy();
    }
}
