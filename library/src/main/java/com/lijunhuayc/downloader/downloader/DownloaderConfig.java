package com.lijunhuayc.downloader.downloader;

import android.os.Environment;

import java.io.File;

/**
 * Desc: 下载器配置
 * Created by ${junhua.li} on 2016/09/28 09:49.
 * Email: lijunhuayc@sina.com
 */
public class DownloaderConfig {
    public static final int DEFAULT_THREAD_NUM = 3;
    private File saveDir;
    private String downloadUrl;
    private int threadNum;

    public DownloaderConfig() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            this.saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        this.threadNum = DEFAULT_THREAD_NUM;
    }

    public File getSaveDir() {
        return saveDir;
    }

    public void setSaveDir(File saveDir) {
        this.saveDir = saveDir;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }


}
