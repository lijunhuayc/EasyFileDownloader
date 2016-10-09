package com.lijunhuayc.downloader.downloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

/**
 * Desc: 小狼文件下载器[简版]
 * Tips: 支持多线程断点续传
 * Created by ${junhua.li} on 2016/09/28 09:59.
 * Email: lijunhuayc@sina.com
 */
public class WolfDownloader {
    DownloaderConfig wolfConfig;
    FileDownloader fileDownloader;

    public WolfDownloader(Context mContext) {
        wolfConfig = new DownloaderConfig();
        fileDownloader = new FileDownloader(mContext);
    }

    @NonNull
    public WolfDownloader setSaveDir(File saveDir) {
        if (null == saveDir) {
            throw new NullPointerException("saveDir is not allow null.");
        }
        wolfConfig.setSaveDir(saveDir);
        return this;
    }

    @NonNull
    public WolfDownloader setDownloadUrl(String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl.trim())) {
            throw new NullPointerException("downloadUrl is not allow null.");
        }
        wolfConfig.setDownloadUrl(downloadUrl);
        return this;
    }

    /**
     * the number between 1-5
     *
     * @param threadNum
     * @return
     */
    public WolfDownloader setThreadNum(int threadNum) {
        wolfConfig.setThreadNum(threadNum < 1 ? 1 : threadNum > 5 ? 5 : threadNum);
        return this;
    }

    public WolfDownloader addDownloadListener(DownloadProgressListener listener) {
        fileDownloader.addDownloadProgressListener(listener);
        return this;
    }

    public void startDownload() {
        if (TextUtils.isEmpty(wolfConfig.getDownloadUrl())) {
            throw new IllegalArgumentException("downloadUrl is not allow null.");
        } else if (null == wolfConfig.getSaveDir()) {
            throw new IllegalArgumentException("saveDir is not allow null.");
        }
        fileDownloader.start(wolfConfig);
    }

//    public void pauseDownload() {
//        fileDownloader.pause();
//    }
//
//    public void stopDownload() {
//        fileDownloader.stop();
//    }

}
