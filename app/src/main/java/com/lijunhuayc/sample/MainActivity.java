package com.lijunhuayc.sample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lijunhuayc.downloader.downloader.DownloadProgressListener;
import com.lijunhuayc.downloader.downloader.DownloaderConfig;
import com.lijunhuayc.downloader.downloader.FileDownloader;
import com.lijunhuayc.downloader.downloader.HistoryCallback;
import com.lijunhuayc.downloader.downloader.WolfDownloader;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends Activity implements View.OnClickListener {
    private EditText downloadpathText;
    private TextView resultView;
    private TextView startBt;
    private TextView stopBt;
    private TextView exitBt;
    private ProgressBar progressBar;
    private Context mContext;
    WolfDownloader wolfDownloader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;

        downloadpathText = (EditText) this.findViewById(R.id.path);
        downloadpathText.setText("http://221.236.21.155/imtt.dd.qq.com/16891/5C119BDFA17906E5D6F45BDF932460BB.apk?mkey=57d63b47fb8efb5e&f=3580&c=0&fsname=com.shangyi.postop.paitent.android_4.2.0.0_18.apk&hsr=4d5s&p=.apk");
//        downloadpathText.setText("https://dl.google.com/dl/android/studio/install/2.2.0.12/android-studio-ide-145.3276617-windows.exe");
//        downloadpathText.setText("http://xmp.down.sandai.net/kankan/XMPSetup_5.2.3.4962.exe");
        progressBar = (ProgressBar) this.findViewById(R.id.downloadbar);
        resultView = (TextView) this.findViewById(R.id.resultView);

        startBt = (TextView) findViewById(R.id.startBt);
        stopBt = (TextView) findViewById(R.id.stopBt);
        exitBt = (TextView) findViewById(R.id.exitBt);
        startBt.setOnClickListener(this);
        stopBt.setOnClickListener(this);
        exitBt.setOnClickListener(this);

        executeInitDownload();

    }

    private void executeInitDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainActivityPermissionsDispatcher.initDwonloadWithCheck(this);
        } else {
            initDwonload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForLocation(final PermissionRequest request) {
        new AlertDialog.Builder(mContext).setTitle("提示")
                .setMessage("下载文件需要文件读写权限")
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForLocation() {
        MyToast.showToast("文件读写权限被拒绝，无法下载");
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForLocation() {
        MyToast.showToast("文件读写权限已被拒绝");
        new AlertDialog.Builder(mContext).setTitle("提示")
                .setMessage("下载文件需要文件读写权限。\n\n请点击\"设置\"-\"权限管理\"-打开所需权限。")
                .setNegativeButton("取消", null)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                })
                .show();
    }

    protected void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void initDwonload() {
        String path = downloadpathText.getText().toString();
        File saveDir = null;
        System.out.println(Environment.getExternalStorageState() + "------" + Environment.MEDIA_MOUNTED);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//开始下载文件
            saveDir = Environment.getExternalStorageDirectory();
        } else {
            Toast.makeText(MainActivity.this, "SDCard不存在或者写保护", Toast.LENGTH_SHORT).show();
        }

        wolfDownloader = new DownloaderConfig()
                .setThreadNum(3)
                .setDownloadUrl(path)
                .setSaveDir(saveDir)
                .setDownloadListener(new DownloadProgressListener() {
                    @Override
                    public void onDownloadTotalSize(int totalSize) {
                        progressBar.setMax(totalSize);//设置进度条的最大刻度为文件的长度
                    }

                    @Override
                    public void updateDownloadProgress(int size, float percent, float speed) {
                        resultView.setText(getFormatStr(size, percent, speed));
                        progressBar.setProgress(size);
                    }

                    @Override
                    public void onDownloadSuccess(String apkPath) {
                        Toast.makeText(MainActivity.this, "下载成功\n" + apkPath, Toast.LENGTH_SHORT).show();
                        startBt.setText("开始");
                        stopBt.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDownloadFailed() {
                        Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        startBt.setText("开始");
                    }

                    @Override
                    public void onPauseDownload() {
                        Toast.makeText(MainActivity.this, "下载暂停", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStopDownload() {
                        Toast.makeText(MainActivity.this, "下载停止", Toast.LENGTH_SHORT).show();
                        startBt.setText("开始");
                        stopBt.setVisibility(View.GONE);
                    }
                })
                .buildWolf(mContext);


        wolfDownloader.readHistory(new HistoryCallback() {
            @Override
            public void onReadHistory(int downloadLength, int fileSize) {
                if (fileSize != 0) {
                    progressBar.setMax(fileSize);
                    progressBar.setProgress(downloadLength);
                    resultView.setText(getFormatStr(downloadLength, FileDownloader.calculatePercent(downloadLength, fileSize), 0));
                }
            }
        });
    }

    private String getFormatStr(int size, float percent, float speed) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("  ").append(FileDownloader.formatSpeed(speed));
        sBuilder.append("  ").append(String.valueOf(percent + "%"));
        sBuilder.append("  ").append(FileDownloader.formatSize(size)).append("/")
                .append(FileDownloader.formatSize(progressBar.getMax()));
        return sBuilder.toString();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBt:
                switch (startBt.getText().toString()) {
                    case "开始":
                        if (null == wolfDownloader) {
                            executeInitDownload();
                        } else {
                            wolfDownloader.startDownload();
                            startBt.setText("暂停");
                            stopBt.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "继续":
                        wolfDownloader.restartDownload();
                        startBt.setText("暂停");
                        break;
                    case "暂停":
                        wolfDownloader.pauseDownload();
                        startBt.setText("继续");
                        break;
                }
                break;
            case R.id.stopBt:
                wolfDownloader.stopDownload();//停止下载会删除下载记录
                break;
            case R.id.exitBt:
                wolfDownloader.exitDownload();//退出下载，保存下载记录
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Process.killProcess(Process.myPid());
        super.onBackPressed();
    }
}