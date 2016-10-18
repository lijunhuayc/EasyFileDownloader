# EasyFileDownloader
A lightweight for use in the android file downloader Download the APK is especially suitable for application in upgrade

### Usage
1、add the followings to your build.gradle file

```java
compile 'com.lijunhuayc.downloader:easyfiledownloader:1.0.3'
```

2、Using the library is really simple, just look at the source code of the provided sample.
```java
WolfDownloader wolfDownloader = new WolfDownloader(context)
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
```
```java
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
