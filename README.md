# EasyFileDownloader
一个用于android中的轻量级文件下载器、特别适合应用内升级下载APK

### 用法：
```java
new WolfDownloader(context)
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
        })
        .startDownload();
```

License
=======

    Copyright 2016 lijunhuayc

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
