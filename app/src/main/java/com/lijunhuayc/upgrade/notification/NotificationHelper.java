package com.lijunhuayc.upgrade.notification;

import android.app.NotificationManager;
import android.content.Context;

import java.util.List;

/**
 * Desc: 通知栏辅助类
 * Created by ${junhua.li} on 2016/09/15 11:47.
 * Email: lijunhuayc@sina.com
 */
public class NotificationHelper {
    private static String TAG = NotificationHelper.class.getSimpleName();
    private static NotificationManager notificationManager = null;

    public static final void init(Context mContext) {
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public void clear() {
        if (null != notificationManager) {
            notificationManager.cancelAll();
        }
    }

    public void remove(int id) {
        if (null != notificationManager) {
            notificationManager.cancel(id);
        }
    }

    public void remove(List<Integer> idList) {
        if (null != notificationManager) {
            for (int id : idList) {
                notificationManager.cancel(id);
            }
        }
    }

}
