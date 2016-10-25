package com.lijunhuayc.downloader.downloader;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/10/25 15:00.
 * Email: lijunhuayc@sina.com
 */
public interface HistoryCallback {
    void onReadHistory(int downloadLength, int fileSize);
}
