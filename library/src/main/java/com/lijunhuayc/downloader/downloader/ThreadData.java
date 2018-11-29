package com.lijunhuayc.downloader.downloader;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/10/25 13:41.
 * Email: lijunhuayc@sina.com
 */
public class ThreadData {
    private int threadId;
    private long downloadLength;
    private long fileSize;

    public ThreadData(int threadId, long fileSize) {
        this.threadId = threadId;
        this.fileSize = fileSize;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "ThreadData{" +
                "threadId=" + threadId +
                ", downloadLength=" + downloadLength +
                ", fileSize=" + fileSize +
                '}';
    }
}
