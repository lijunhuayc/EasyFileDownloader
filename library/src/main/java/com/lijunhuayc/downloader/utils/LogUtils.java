package com.lijunhuayc.downloader.utils;

import android.support.v4.BuildConfig;
import android.util.Log;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/10/17 12:35.
 * Email: lijunhuayc@sina.com
 */

public class LogUtils {

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

}
