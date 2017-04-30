package com.hejunlin.imooc_supervideo.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Settings;

/**
 * Created by hejunlin on 17/4/30.
 */

public class SysUtils {

    //获取亮度
    public static int getBrightness(Context context) {
        return Settings.System.getInt(context.getContentResolver(),"screen_brightness", -1);
    }
    //设置亮度
    public static void setBrightness(Context context, int param) {
        Settings.System.putInt(context.getContentResolver(), "screen_brightness", param);
    }
    //获取亮度的sharedPreferences文件
    public static int getDefaultBrightness(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("shared_preferences_light", -1);
    }

}
