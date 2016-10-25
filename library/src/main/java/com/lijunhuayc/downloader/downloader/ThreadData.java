package com.lijunhuayc.downloader.downloader;

/**
 * Desc:
 * Created by ${junhua.li} on 2016/10/25 13:41.
 * Email: lijunhuayc@sina.com
 */
public class ThreadData {
    private int threadId;
    private int downloadLength;
    private int fileSize;

    public ThreadData(int threadId, int fileSize) {
        this.threadId = threadId;
        this.fileSize = fileSize;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getDownloadLength() {
        return downloadLength;
    }

    public void setDownloadLength(int downloadLength) {
        this.downloadLength = downloadLength;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
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
