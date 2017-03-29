package com.lijunhuayc.sample;

import android.app.Application;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/09/28 09:33.
 * Email: lijunhuayc@sina.com
 */
public class MyApplication extends Application {
    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MyToast.init(this);
    }

    public static MyApplication getApplication() {
        return application;
    }

}
