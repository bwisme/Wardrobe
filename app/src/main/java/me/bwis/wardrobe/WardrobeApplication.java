package me.bwis.wardrobe;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.bwis.wardrobe.utils.Constant;
import me.bwis.wardrobe.utils.Res;
import me.bwis.wardrobe.utils.SharedPreferenceUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        ApplicationState.IS_SHOWING_ALL_TYPE = false;
        ApplicationState.CURRENT_CATEGORY = SharedPreferenceUtils.instance.getInt(Constant.PREF_CATEGORY, Constant.CATEGORY_TYPE);
        getWeather();
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
        public static boolean IS_SHOWING_ALL_TYPE;
        public static HashMap<String, String> weatherStatus = new HashMap<>();
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

    private void getWeather()
    {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url("http://t.weather.sojson.com/api/weather/city/101010100").method("GET",null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject cityInfo = jsonObject.getJSONObject("cityInfo");
                    ApplicationState.weatherStatus.put("city", cityInfo.getString("city"));
                    JSONArray forecast = jsonObject.getJSONObject("data").getJSONArray("forecast");
                    JSONObject today = forecast.getJSONObject(0);
                    ApplicationState.weatherStatus.put("todayMax", today.getString("high").split(" ")[1]);
                    ApplicationState.weatherStatus.put("todayMin", today.getString("low").split(" ")[1]);
                    ApplicationState.weatherStatus.put("type", today.getString("type"));
                    ApplicationState.weatherStatus.put("notice", today.getString("notice"));
                    JSONObject tomorrow = forecast.getJSONObject(1);
                    ApplicationState.weatherStatus.put("tomorrowMax", tomorrow.getString("high").split(" ")[1]);
                    ApplicationState.weatherStatus.put("tomorrowMin", tomorrow.getString("low").split(" ")[1]);


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

    }



}
