package com.lijunhuayc.downloader.downloader;

/**
 * Desc: 下载进度接口
 * Created by ${junhua.li} on 2016/08/25 17:38.
 * Email: lijunhuayc@sina.com
 */
public interface DownloadProgressListener {
    void onDownloadTotalSize(int totalSize);

    /**
     * 实时更新下载进度
     *
     * @param size    当前下载完成 单位 Byte
     * @param percent 当前下载百分比 单位 %
     * @param speed   当前下载速度 单位 KB/S
     */
    void updateDownloadProgress(int size, float percent, float speed);

    void onDownloadSuccess(String apkPath);

    void onDownloadFailed();
}
