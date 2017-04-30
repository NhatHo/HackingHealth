package hackinghealth.com.whatsapnea;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nhatho on 2017-04-29.
 */

public class CameraUtilities {
    private static final String TAG = "CameraUtilities";
    private static final String VIDEO_PREFIX = "SLEEP_APNEA_";
    private static final String APP_ALBUM = "SleepApnea";
    private static final String VIDEO_EXTENSION = ".mp4";

    public CameraUtilities() {}

    public File getVideosAlbumDirectory(String albumName) {
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES
                ),
                albumName
        );
    }

    private File createAlbumDirectory() {
        File storageDir = null;
        String todayAlbum = APP_ALBUM + "/" + new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getVideosAlbumDirectory(todayAlbum);
            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists() || !storageDir.canWrite()){
                        Log.d(TAG, "Failed to create a writable directory");
                        return null;
                    }
                }
            }
        }
        return storageDir;
    }

    public File CreateVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = VIDEO_PREFIX + timeStamp + "_";
        File videoAlbum = createAlbumDirectory();
        File videoFile = File.createTempFile(videoFileName, VIDEO_EXTENSION, videoAlbum);
        return videoFile;
    }
}