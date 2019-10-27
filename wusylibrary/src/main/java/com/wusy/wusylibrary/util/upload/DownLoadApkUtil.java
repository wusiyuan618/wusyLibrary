package com.wusy.wusylibrary.util.upload;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by XIAO RONG on 2018/7/19.
 */

public class DownLoadApkUtil {
    protected static String FILEDIR = Environment.getExternalStorageDirectory() + "/Wusy/";
    protected static final String DOWNLOAD_ACTION = "download_action";
    protected static String Custom_DOWNLOADAPK_URL = "";
    private  String APPNAME = "hjl";
    private BaseActivity activity;
    private ProgressDialog m_pDialog;

    public DownLoadApkUtil(BaseActivity activity) {
        this.activity = activity;
    }
    public DownLoadApkUtil(BaseActivity activity,String dir,String appName) {
        this.activity = activity;
        this.FILEDIR=dir;
        this.APPNAME=appName;
    }
    /**
     * 开始下载并更新
     */
    public void start(String path, String content) {
        showDialog(new DownLoadBroadCastReceiver(), content);
        DownLoadApkUtil.Custom_DOWNLOADAPK_URL = path;
    }

    /***
     * 检查是否更新版本
     */
    private void showDialog(final BroadcastReceiver receiver, String content) {
        final Dialog alert = new UpdateDialog(activity, R.style.UploadDialogStyle);
        alert.setContentView(R.layout.dialog_upgrade_layout);
        TextView tvView = alert.findViewById(R.id.tv_msg);
        tvView.setText(content);

        Button sureBtn = alert.findViewById(R.id.versionchecklib_version_dialog_commit);
        ImageView cancleBtn = alert.findViewById(R.id.versionchecklib_version_dialog_cancel);
        sureBtn.setOnClickListener(v -> {
            ArrayList<String> actionList = new ArrayList<>();
            actionList.add(DownLoadApkUtil.DOWNLOAD_ACTION);
            activity.addBroadcastAction(actionList, receiver);
            Intent updateIntent = new Intent(activity,
                    UpdateService.class);
            updateIntent.putExtra("app_name", APPNAME);
            activity.startService(updateIntent);
            alert.dismiss();
        });
        cancleBtn.setOnClickListener(v -> alert.dismiss());

        alert.show();
    }

    class DownLoadBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case DownLoadApkUtil.DOWNLOAD_ACTION:
                    int progress = intent.getIntExtra("download", 0);
                    showProgress(progress);
                    break;
                default:
                    break;
            }

        }
    }

    private void showProgress(Integer progress) {
        if (m_pDialog == null) {
            createProgressDialog("正在更新请稍后...");
        }
        m_pDialog.setProgress(progress);
        if (progress == 100) {
            m_pDialog.dismiss();
        }
    }

    private void createProgressDialog(String title) {
        // 创建ProgressDialog对象
        m_pDialog = new ProgressDialog(activity);
        // 设置进度条风格，风格为长形
        m_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        m_pDialog.setTitle(title);
        // 设置ProgressDialog 进度条进度
        m_pDialog.setProgress(100);
        // 设置ProgressDialog 的进度条是否不明确
        m_pDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        m_pDialog.setCancelable(true);
        // 设置点击进度对话框外的区域对话框不消失
        m_pDialog.setCanceledOnTouchOutside(false);
        // 让ProgressDialog显示
        m_pDialog.show();
    }
}
