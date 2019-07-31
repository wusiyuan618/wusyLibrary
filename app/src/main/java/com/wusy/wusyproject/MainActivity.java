package com.wusy.wusyproject;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.text.DecimalFormat;





public class MainActivity extends AppCompatActivity{
    String TAG = "wsy";
    private long baseTimer;
    private TextView timerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.baseTimer = SystemClock.elapsedRealtime();
        timerView = (TextView) this.findViewById(R.id.timerView);
        Handler myhandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (0 == baseTimer) {
                    baseTimer = SystemClock.elapsedRealtime();
                }

                int time = (int) ((SystemClock.elapsedRealtime() - baseTimer) / 1000);
                String mm = new DecimalFormat("00").format(time / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                if (null != timerView) {
                    timerView.setText(mm + ":" + ss);
                }
                Message message = Message.obtain();
                message.what = 0x0;
                sendMessageDelayed(message, 1000);
            }
        };
        myhandler.sendMessageDelayed(Message.obtain(myhandler, 1), 1000);
    }
}
