package com.simple.testdownload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simple.download.DLListener;
import com.simple.download.DLManager;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private TextView tvDlInfo;
    private TextView tvDlAverage;
    private DLManager dlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.ftp_btn_start);
        tvDlInfo = (TextView) findViewById(R.id.ftp_tv_dl_info);
        tvDlAverage = (TextView) findViewById(R.id.ftp_tv_dl_average);
        initEvents();
    }

    boolean isStart = false;

    public void initEvents() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart) {
                    btnStart.setText("停止下载");
                    startMutitaskDownload();
                } else {
                    if (dlManager != null) {
                        dlManager.cancel();
                        btnStart.setText("开始下载");
                    }
                }
                isStart = !isStart;
            }
        });
    }

    String downloadUrl = "http://vjs.zencdn.net/v/oceans.mp4";
    //    String downloadUrl = "ftp://d:d@a.dygodj8.com:12311/xxx.mkv ";
    //    String filepath = PathUtils.getCachePath() + "UC-11.5.5.943.apk";
    String filepath = PathUtils.getCachePath();

    /**
     * 多线程下载
     */
    private void startMutitaskDownload() {
        dlManager = new DLManager();
        dlManager.download(downloadUrl, filepath, 2, new DLListener() {
            @Override
            public void onDLStart() {

            }

            @Override
            public void onDLFailed(String msg) {

            }

            @Override
            public void onDLFinished(final long costTime, final long dlSize, String filePath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (costTime != 0) {
                            DecimalFormat dFormat = new DecimalFormat("##0.00");
                            Double res = dlSize * 1.0 / 1024 / costTime;
                            String speed = dFormat.format(res);
                            tvDlAverage.setText("平均下载速度：" + speed + "Mb/s ");
                        }
                    }
                });
            }

            @Override
            public void onProgressChange(final long fileSize, final long loadedSize, final long changeSize) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDlInfo.setText("下载速度：" + changeSize / 1024 / 1024 + "Mb/s " +
                                "下载进度：" + loadedSize / (fileSize / 100) + "%");
                    }
                });
            }
        });
    }
}
