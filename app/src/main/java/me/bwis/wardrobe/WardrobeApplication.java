package me.bwis.wardrobe;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import java.util.HashSet;
import java.util.Set;

import me.bwis.wardrobe.utils.Constant;
import me.bwis.wardrobe.utils.Res;
import me.bwis.wardrobe.utils.SharedPreferenceUtils;

public class WardrobeApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        AVOSCloud.initialize(this, "KHErkm74YnjSwLC24PX7Jv7M-gzGzoHsz","rGFU3lIQKFJnbCyai9PKNgXG");
        AVOSCloud.setDebugLogEnabled(true);
        SharedPreferenceUtils.setInstance(getApplicationContext().getSharedPreferences(Constant.PREF_FILE, MODE_PRIVATE));
        ApplicationState.IS_LOGGED_IN = false;
        ApplicationState.CURRENT_CATEGORY = SharedPreferenceUtils.instance.getInt(Constant.PREF_CATEGORY, Constant.CATEGORY_TYPE);
        initPreferences();
    }

    private void initPreferences()
    {
        initLoginState();
//        this.deleteDatabase(ClothesItemDBHelper.DB_NAME); // danger!!!!
        //Type set
        Set<String> typeSet = SharedPreferenceUtils.getStringSet(Constant.PREF_TYPE_SET, new HashSet<String>());
        if (typeSet.isEmpty())
        {
            //first time
            typeSet.add("Coat");
            typeSet.add("Down Jacket");
            typeSet.add("Sweater");
            typeSet.add("Shirt & Blouse");
            typeSet.add("T-Shirt");
            typeSet.add("Casual Pants");
            typeSet.add("Trousers");
            typeSet.add("Sweatpants");
            typeSet.add("Skirt");
            typeSet.add("Dress");
            typeSet.add("Jacket");
            typeSet.add("Sportswear");
            typeSet.add("Jeans");
            typeSet.add("Shorts");
            typeSet.add("Underwear");
            typeSet.add("Accessories");
            typeSet.add("Bag");
            typeSet.add("Leather Shoes");
            typeSet.add("Sneakers");
            typeSet.add("Casual Shoes");
            typeSet.add("Boots");
            SharedPreferenceUtils.putStringSet(Constant.PREF_TYPE_SET, typeSet);
        }
        Res.TYPE_SET = typeSet;
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onTerminate()
    {
        super.onTerminate();
        SharedPreferenceUtils.putStringSet(Constant.PREF_TYPE_SET, Res.TYPE_SET);
        SharedPreferenceUtils.instance.edit().commit();
    }

    public static class ApplicationState
    {
        public static boolean IS_LOGGED_IN;
        public static int CURRENT_CATEGORY;
    }

    private void initLoginState()
    {
        String lastSessionToken = SharedPreferenceUtils.getString(Constant.PREF_USER_TOKEN, "");
        if (lastSessionToken == "" || WardrobeApplication.ApplicationState.IS_LOGGED_IN)
            return;
        else
        {
            //login
            AVUser.becomeWithSessionTokenInBackground(lastSessionToken, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e)
                {
                    if (e == null)
                    {
                        ApplicationState.IS_LOGGED_IN = true;
                    }
                    else
                    {
                        return;
                    }
                }
            });
        }
    }



}
