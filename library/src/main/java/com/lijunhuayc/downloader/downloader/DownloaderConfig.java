package com.lijunhuayc.downloader.downloader;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/09/28 09:49.
 * Email: lijunhuayc@sina.com
 */
public class DownloaderConfig {
    public static final int DEFAULT_THREAD_NUM = 3;
    private File saveDir;
    private String downloadUrl;
    private int threadNum;
    private DownloadProgressListener downloadListener;

    public DownloaderConfig() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            this.saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        this.threadNum = DEFAULT_THREAD_NUM;
    }

    public File getSaveDir() {
        return saveDir;
    }

    public DownloaderConfig setSaveDir(File saveDir) {
        if (null == saveDir) {
            throw new IllegalArgumentException("the saveDir is not allow null.");
        }
        this.saveDir = saveDir;
        return this;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public DownloaderConfig setDownloadUrl(String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl.trim())) {
            throw new IllegalArgumentException("the downloadUrl is not allow null.");
        }
        this.downloadUrl = downloadUrl;
        return this;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public DownloaderConfig setThreadNum(int threadNum) {
        if (threadNum < 1 || threadNum > 5) {
            throw new IllegalArgumentException("the threadNum between 1-5.");
        }
        this.threadNum = threadNum;
        return this;
    }

    public DownloaderConfig setDownloadListener(DownloadProgressListener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    public DownloadProgressListener getDownloadListener() {
        return downloadListener;
    }

    public WolfDownloader buildWolf(Context mContext) {
        return new WolfDownloader(mContext, this);
    }

}
