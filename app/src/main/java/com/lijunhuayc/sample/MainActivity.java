package com.lijunhuayc.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lijunhuayc.downloader.downloader.DownloadProgressListener;
import com.lijunhuayc.downloader.downloader.FileDownloader;
import com.lijunhuayc.downloader.downloader.WolfDownloader;

public class MainActivity extends Activity implements View.OnClickListener {
    private EditText downloadpathText;
    private TextView resultView;
    private ProgressBar progressBar;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;

        downloadpathText = (EditText) this.findViewById(R.id.path);
        downloadpathText.setText("http://221.236.21.155/imtt.dd.qq.com/16891/5C119BDFA17906E5D6F45BDF932460BB.apk?mkey=57d63b47fb8efb5e&f=3580&c=0&fsname=com.shangyi.postop.paitent.android_4.2.0.0_18.apk&hsr=4d5s&p=.apk");
//        downloadpathText.setText("https://dl.google.com/dl/android/studio/install/2.2.0.12/android-studio-ide-145.3276617-windows.exe");
        progressBar = (ProgressBar) this.findViewById(R.id.downloadbar);
        resultView = (TextView) this.findViewById(R.id.resultView);

        findViewById(R.id.startBt).setOnClickListener(this);
        findViewById(R.id.pauseBt).setOnClickListener(this);
        findViewById(R.id.continueBt).setOnClickListener(this);
        findViewById(R.id.stopBt).setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBt:
                startDownload();
                break;
            case R.id.pauseBt:
                wolfDownloader.pauseDownload();
                break;
            case R.id.continueBt:
                wolfDownloader.restartDownload();
                break;
            case R.id.stopBt:
                wolfDownloader.stopDownload();
                break;
        }
    }

    WolfDownloader wolfDownloader;

    private void startDownload() {
        String path = downloadpathText.getText().toString();
        System.out.println(Environment.getExternalStorageState() + "------" + Environment.MEDIA_MOUNTED);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//开始下载文件
            wolfDownloader = new WolfDownloader(context)
                    .setThreadNum(3)
                    .setDownloadUrl(path)
                    .setSaveDir(Environment.getExternalStorageDirectory())
                    .addDownloadListener(new DownloadProgressListener() {
                        @Override
                        public void onDownloadTotalSize(int totalSize) {
                            progressBar.setMax(totalSize);//设置进度条的最大刻度为文件的长度
                        }

                        @Override
                        public void updateDownloadProgress(int size, float percent, float speed) {
                            StringBuilder sBuilder = new StringBuilder();
                            sBuilder.append("  ").append(FileDownloader.formatSpeed(speed));
                            sBuilder.append("  ").append(String.valueOf(percent + "%"));
                            sBuilder.append("  ").append(FileDownloader.formatSize(size)).append("/")
                                    .append(FileDownloader.formatSize(progressBar.getMax()));
                            resultView.setText(sBuilder.toString());
                            progressBar.setProgress(size);

                        }

                        @Override
                        public void onDownloadSuccess(String apkPath) {
                            Toast.makeText(MainActivity.this, "下载成功\n" + apkPath, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDownloadFailed() {
                            Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPauseDownload() {

                        }

                        @Override
                        public void onStopDownload() {

                        }
                    });
            wolfDownloader.startDownload();
        } else {
            Toast.makeText(MainActivity.this, "SDCard不存在或者写保护", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Process.killProcess(Process.myPid());
        super.onBackPressed();
    }
}