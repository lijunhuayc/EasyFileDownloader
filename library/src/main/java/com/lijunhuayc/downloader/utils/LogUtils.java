package com.lijunhuayc.downloader.utils;

import android.util.Log;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/10/17 12:35.
 * Email: lijunhuayc@sina.com
 */

public class LogUtils {
    private static final boolean LOG_SWITCH = true;

    public static void d(String tag, String msg) {
        if (!LOG_SWITCH) {
            Log.d(tag, msg);
        }
    }

}
