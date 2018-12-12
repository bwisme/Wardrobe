package me.bwis.wardrobe;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class WardrobeApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        AVOSCloud.initialize(this, "KHErkm74YnjSwLC24PX7Jv7M-gzGzoHsz","rGFU3lIQKFJnbCyai9PKNgXG");
        AVOSCloud.setDebugLogEnabled(true);
        ApplicationState.IS_LOGGED_IN = false;
    }

    public static class ApplicationState
    {

        public static boolean IS_LOGGED_IN;

    }

}
