package com.lijunhuayc.upgrade.downloader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.lijunhuayc.upgrade.db.DownloadDBHelper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desc: 文件下载器
 * Created by ${junhua.li} on 2016/08/24 15:12.
 * Email: lijunhuayc@sina.com
 */
public class FileDownloader {
    private static final String TAG = FileDownloader.class.getSimpleName();
    //    protected static final int DOWNLOAD_STATUS_NONE = -1;//未启动或者已终止
//    protected static final int DOWNLOAD_STATUS_START = 1;//正在下载
//    protected static final int DOWNLOAD_STATUS_PAUSE = 2;//暂停下载
//    protected static final int DOWNLOAD_STATUS_STOP = 3;//停止下载
    private Context context;
    private DownloadDBHelper downloadDBHelper;
    private List<DownloadProgressListener> progressListeners = new ArrayList<>();
    private DownloaderConfig config;
    private int fileSize = 0;
    private DownloadThread[] threads;
    private File saveFile;
    private Map<Integer, Integer> data = new ConcurrentHashMap<>();
    private int downloadSize = 0;
    private int lastDownloadSize = 0;//最后一次更新时下载进度
    private int block;
    private Handler mHandler;
//    private int downloadStatus = DOWNLOAD_STATUS_NONE; //

    public FileDownloader(Context mContext) {
        this.context = mContext;
        this.downloadDBHelper = DownloadDBHelper.getInstance(mContext, "file_download.db");
        this.mHandler = new Handler(this.context.getMainLooper());
    }

//    protected void setDownloadStatus(int downloadStatus) {
//        this.downloadStatus = downloadStatus;
//    }
//
//    public int getDownloadStatus() {
//        return downloadStatus;
//    }

//    protected void pause() {
//        setDownloadStatus(FileDownloader.DOWNLOAD_STATUS_PAUSE);
//    }
//
//    protected void stop() {
//        setDownloadStatus(FileDownloader.DOWNLOAD_STATUS_STOP);
//    }

    protected void start(final DownloaderConfig config) {
        new Thread() {
            @Override
            public void run() {
                prepareDownload(config);
            }
        }.start();
    }

    private void prepareDownload(DownloaderConfig config) {
        this.config = config;
        this.threads = new DownloadThread[config.getThreadNum()];
        if (!this.config.getSaveDir().exists()) {
            if (!this.config.getSaveDir().mkdirs()) {
                Log.d(TAG, "mkdirs download directory failed.");
                return;
            }
        }

        HttpURLConnection urlConn = openConnection(config.getDownloadUrl());
        try {
            if (urlConn.getResponseCode() == 200) {
                this.fileSize = urlConn.getContentLength();
                if (this.fileSize <= 0) {
                    throw new RuntimeException("unKnow file size");
                }
                onDownloadTotalSize(this.fileSize);
            } else {
                throw new RuntimeException("server no response.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "open HttpURLConnection failed.");
            return;
        }

        this.saveFile = new File(this.config.getSaveDir(), getFileName(urlConn, config.getDownloadUrl()));
        Map<Integer, Integer> recordMap = downloadDBHelper.query(config.getDownloadUrl());
        if (recordMap.size() > 0) {
            for (Map.Entry<Integer, Integer> entry : recordMap.entrySet()) {
                this.data.put(entry.getKey(), entry.getValue());
            }
        }
        if (this.data.size() == this.threads.length) {
            for (int i = 0; i < this.threads.length; i++) {
                this.downloadSize += this.data.get(i);
            }
            Log.d(TAG, "已经下载的长度" + this.downloadSize);
        } else {
            this.data.clear();
            for (int i = 0; i < this.threads.length; i++) {
                this.data.put(i, 0);//初始化每条线程已经下载的数据长度为0
            }
            this.downloadSize = 0;
            this.downloadDBHelper.save(this.config.getDownloadUrl(), this.data);//初次保存数据
        }

        //计算每条线程下载的最大数据长度
        this.block = (this.fileSize % this.threads.length) == 0 ?
                this.fileSize / this.threads.length :
                this.fileSize / this.threads.length + 1;

        executeDownload();
    }

    private void executeDownload() {
        try {
            RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rw");
            if (this.fileSize > 0) {
                randOut.setLength(this.fileSize);
            }
            randOut.close();
            URL url = new URL(this.config.getDownloadUrl());

            for (int i = 0; i < this.threads.length; i++) {//开启线程进行下载
                int downLength = this.data.get(i);
                if (downLength < this.block && this.downloadSize < this.fileSize) {
                    this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i), i);
                    this.threads[i].setPriority(7);
                    this.threads[i].start();
                } else {
                    this.threads[i] = null;
                }
            }

            this.lastDownloadSize = this.downloadSize;
            long startTime;
            boolean notFinish = true;
            while (notFinish) {
                startTime = System.currentTimeMillis();
                Thread.sleep(1000);
                notFinish = false;
                for (int i = 0; i < this.threads.length; i++) {
                    if (this.threads[i] != null && !this.threads[i].isFinish()) {
                        notFinish = true;
                        if (this.threads[i].getDownloadLength() == -1) {//线程异常时重启下载线程
                            this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, this.data.get(i), i);
                            this.threads[i].setPriority(7);
                            this.threads[i].start();
                        }
                    }
                }
                onDownloadSize(this.downloadSize, calculatePercent(), calculateSpeed(startTime, System.currentTimeMillis()));
                this.lastDownloadSize = this.downloadSize;
            }
            onDownloadSize(this.downloadSize, 100.0f, 0.0f);    //下载结束后需要回调一次更新下载速度为0
            downloadDBHelper.delete(this.config.getDownloadUrl());
            if (this.downloadSize == this.fileSize) {
                onDownloadSuccess(saveFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "file download failed.");
            onDownloadFailed();
        }
    }

    protected HttpURLConnection openConnection(String downloadUrl) {
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", downloadUrl);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
//            printResponseHeader(conn);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("don't connection this url.");
        }
    }

    private String getFileName(HttpURLConnection conn, String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        if (TextUtils.isEmpty(fileName)) {
            for (int i = 0; ; i++) {
                String mine = conn.getHeaderField(i);
                if (mine == null) {
                    break;
                }
                if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if (m.find()) {
                        return m.group(1);
                    }
                }
            }
            fileName = UUID.randomUUID() + ".tmp";
        }
        return fileName;
    }

    private void onDownloadSize(final int size, final float percent, final float speed) {
        for (final DownloadProgressListener listener : progressListeners) {
            if (null != listener) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.updateDownloadProgress(size, percent, speed);
                        }
                    });
                } else {
                    listener.updateDownloadProgress(size, percent, speed);
                }
            }
        }
    }

    private void onDownloadTotalSize(final int totalSize) {
        for (final DownloadProgressListener listener : progressListeners) {
            if (null != listener) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadTotalSize(totalSize);
                        }
                    });
                } else {
                    listener.onDownloadTotalSize(totalSize);
                }
            }
        }
    }

    private void onDownloadSuccess(final String apkPath) {
        if (null == progressListeners) return;
        for (final DownloadProgressListener listener : progressListeners) {
            if (null != listener) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadSuccess(apkPath);
                        }
                    });
                } else {
                    listener.onDownloadSuccess(apkPath);
                }
            }
        }
    }

    private void onDownloadFailed() {
        if (null == progressListeners) return;
        for (final DownloadProgressListener listener : progressListeners) {
            if (null != listener) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadFailed();
                        }
                    });
                } else {
                    listener.onDownloadFailed();
                }
            }
        }
    }

    public void addDownloadProgressListener(DownloadProgressListener listener) {
        if (null != listener) {
            progressListeners.add(listener);
        }
    }

    protected synchronized void append(int size) {
        downloadSize += size;
    }

    protected synchronized void update(int threadId, int pos) {
        this.data.put(threadId, pos);
        this.downloadDBHelper.update(this.config.getDownloadUrl(), this.data);
    }

    static final long GB_CONSTANT = 1024 * 1024 * 1024;//1GB
    static final long MB_CONSTANT = 1024 * 1024;//1MB
    static final long KB_CONSTANT = 1024;//1KB

    /**
     * 格式化下载进度
     *
     * @param size unit Byte
     * @return
     */
    public static final String formatSize(int size) {
        if (size < MB_CONSTANT) {
            float kSize = (float) size / KB_CONSTANT;
            kSize = ((float) ((int) (kSize * 10))) / 10;
            return kSize + "K";
        } else if (size < GB_CONSTANT) {
            float kSize = (float) size / MB_CONSTANT;
            kSize = ((float) ((int) (kSize * 10))) / 10;
            return kSize + "M";
        } else {
            float kSize = (float) size / GB_CONSTANT;
            kSize = ((float) ((int) (kSize * 100))) / 100;
            return kSize + "G";
        }
    }

    /**
     * 格式化下载速度
     *
     * @param speed unit Byte
     * @return
     */
    public static final String formatSpeed(float speed) {
        float realSpeed = speed * KB_CONSTANT;
        if (realSpeed < KB_CONSTANT) {
            float kSize = realSpeed / KB_CONSTANT;
            kSize = ((float) ((int) (kSize * 10))) / 10;
            return kSize + " B/s";
        } else if (realSpeed < MB_CONSTANT) {
            float kSize = realSpeed / KB_CONSTANT;
            kSize = ((float) ((int) (kSize * 10))) / 10;
            return kSize + " KB/s";
        } else if (realSpeed < GB_CONSTANT) {
            float kSize = realSpeed / MB_CONSTANT;
            kSize = ((float) ((int) (kSize * 10))) / 10;
            return kSize + " MB/s";
        } else {
            float kSize = realSpeed / GB_CONSTANT;
            kSize = ((float) ((int) (kSize * 100))) / 100;
            return kSize + " GB/s";
        }
    }

    /**
     * 格式化下载百分比
     *
     * @return
     */
    public static String formatPercent(int downloadSize, int fileSize) {
        float num = (float) downloadSize / fileSize;
        float percent = ((float) (int) (num * 1000)) / 10;
        return String.valueOf(percent + "%");
    }

    /**
     * 计算下载百分比
     *
     * @return
     */
    private float calculatePercent() {
        float num = (float) this.downloadSize / this.fileSize;
        float percent = ((float) (int) (num * 1000)) / 10;
//        Log.d(TAG, "下载百分比：" + percent + "%");
        return percent;
    }

    /**
     * 计算实时下载速度
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private float calculateSpeed(long startTime, long endTime) {
        float usedTime = ((float) (endTime - startTime)) / 1000;
        float speed = 0;
        if (usedTime > 0) {
            int mSize = this.downloadSize - this.lastDownloadSize;
            speed = ((float) mSize / usedTime) / KB_CONSTANT;
            speed = ((float) ((int) (speed * 10))) / 10;
        }
//        Log.d(TAG, "下载速度：" + speed + " KB/s");
        return speed;
    }

    private static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0; ; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null) break;
            header.put(http.getHeaderFieldKey(i), mine);
        }

        return header;
    }

    private static void printResponseHeader(HttpURLConnection http) {
        Map<String, String> header = getHttpResponseHeader(http);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey() + ":" : "";
            Log.d(TAG, key + entry.getValue());
        }
    }

}
