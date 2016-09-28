package com.lijunhuayc.upgrade.downloader;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Desc: 下载线程
 * Created by ${junhua.li} on 2016/08/24 15:08.
 * Email: lijunhuayc@sina.com
 */
public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private FileDownloader fileDownloader;
    private URL downUrl;
    private File saveFile;
    private int block;
    private int downloadLength = -1;
    private int threadId = -1;
    private boolean finish = false;

    public DownloadThread(FileDownloader fileDownloader, URL downUrl, File saveFile, int block, int downloadLength, int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.fileDownloader = fileDownloader;
        this.threadId = threadId;
        this.downloadLength = downloadLength;
    }

    @Override
    public void run() {
        if (this.downloadLength < this.block) {
            InputStream inStream = null;
            RandomAccessFile randomAccessFile = null;
            try {
                int startPos = this.block * (this.threadId) + this.downloadLength;
                int endPos = this.block * (this.threadId + 1) - 1;

                HttpURLConnection httpURLConnection = (HttpURLConnection) this.downUrl.openConnection();
                httpURLConnection.setConnectTimeout(5 * 1000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                httpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
                httpURLConnection.setRequestProperty("Referer", this.downUrl.toString());
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);//设置获取实体数据的范围
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                Log.d(TAG, "Thread " + this.threadId + " start download from position " + startPos);

                inStream = httpURLConnection.getInputStream();
                byte[] buffer = new byte[1024 * 1024];
                int offset = 0;

                randomAccessFile = new RandomAccessFile(this.saveFile, "rwd");
                randomAccessFile.seek(startPos);
                while ((offset = inStream.read(buffer, 0, buffer.length)) != -1) {
                    randomAccessFile.write(buffer, 0, offset);
                    this.downloadLength += offset;
                    this.fileDownloader.update(this.threadId, this.downloadLength);
                    this.fileDownloader.append(offset);
//                    Log.d(TAG, "Thread" + this.threadId + " downloadLength=" + this.downloadLength + ", offset=" + offset);
                }
                Log.d(TAG, "Thread " + this.threadId + " download finish");
                this.finish = true;
            } catch (Exception e) {
                this.downloadLength = -1;
                Log.d(TAG, "Thread " + this.threadId + ":" + e.getMessage());
            } finally {
                try {
                    if (null != randomAccessFile) {
                        randomAccessFile.close();
                    }
                } catch (Exception e) {
                }
                try {
                    if (null != inStream) {
                        inStream.close();
                    }
                } catch (Exception e) {
                }

            }
        }
    }

    public boolean isFinish() {
        return finish;
    }

    public long getDownloadLength() {
        return downloadLength;
    }
}
