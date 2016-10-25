package com.lijunhuayc.downloader.downloader;

import android.content.Context;

/**
 * Desc: Wolf File Downloader [Simple]
 * Tips: Support multithreading breakpoint continuingly
 * Created by ${junhua.li} on 2016/09/28 09:59.
 * Email: lijunhuayc@sina.com
 */
public class WolfDownloader {
    FileDownloader fileDownloader;

    public WolfDownloader(Context mContext, DownloaderConfig config) {
        this.fileDownloader = new FileDownloader(mContext);
        this.fileDownloader.setConfig(config);
    }

    public void readHistory(HistoryCallback historyCallback){
        this.fileDownloader.readHistory(historyCallback);
    }

    public void startDownload() {
        fileDownloader.start();
    }

    public void pauseDownload() {
        fileDownloader.pause();
    }

    public void restartDownload() {
        fileDownloader.restart();
    }

    public void stopDownload() {
        fileDownloader.stop();
    }

}
