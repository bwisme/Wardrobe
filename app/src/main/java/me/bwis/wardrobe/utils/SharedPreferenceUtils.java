package me.bwis.wardrobe.utils;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceUtils
{

    public static SharedPreferences instance = null;

    public static String getString(String key, String defValue)
    {
        if (instance == null)
            return defValue;
        return instance.getString(key, defValue);

    }

    public static Set<String> getStringSet(String key, Set<String> defValue)
    {
        if (instance == null)
            return defValue;
        return instance.getStringSet(key, defValue);
    }

    public static void putString(String key, String value)
    {
        if (instance == null)
            return;
        instance.edit().putString(key, value).apply();
    }

    public static void putStringToStringSet(String key, String value)
    {
        if (instance == null)
            return;
        Set<String> set = new HashSet<>(SharedPreferenceUtils.getStringSet(key, new HashSet<String>()));
        set.add(value);
        instance.edit().putStringSet(key, set).apply();
    }

    public static void removeStringFromStringSet(String key, String value)
    {
        if (instance == null)
            return;
        Set<String> set = new HashSet<>(SharedPreferenceUtils.getStringSet(key, new HashSet<String>()));
        if (set.isEmpty())
            return;
        set.remove(value);
        instance.edit().putStringSet(key, set).apply();
    }


    public static void putStringSet(String key, Set<String> values)
    {
        if (instance == null)
            return;
        instance.edit().putStringSet(key, values).apply();
    }

    public static void setInstance(SharedPreferences instance)
    {
        SharedPreferenceUtils.instance = instance;
    }
}
