package com.lijunhuayc.upgrade.sample;

import android.app.Application;

import com.lijunhuayc.upgrade.notification.NotificationHelper;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/09/28 09:33.
 * Email: lijunhuayc@sina.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.init(this);
    }
}
