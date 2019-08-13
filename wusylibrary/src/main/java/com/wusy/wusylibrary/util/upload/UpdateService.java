package com.wusy.wusylibrary.util.upload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.wusy.wusylibrary.util.CommonUtil;
import com.wusy.wusylibrary.util.OkHttpUtil;

import java.io.File;

/**
 *
 * Filename: UpdateService.java Description: 今天修改了当增量包合成失败的时候，重新下载整个最新的apk
 * Company: minto
 *
 */
public class UpdateService extends Service {
	private String app_name;
	private String file_name;
	private File updateDir = new File(DownLoadApkUtil.FILEDIR);
	File updateFile;
	String patchUrl = DownLoadApkUtil.FILEDIR;
	String newApkUrl = DownLoadApkUtil.FILEDIR;
	private String downUrl = "";
	private int flag = 2;
	private Intent broadCast;
	@Override
	public IBinder onBind(Intent arg0) {
		stopSelf();
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (CommonUtil.isNull(intent)) {
			stopSelf();
			Log.d("UpService", intent + "--------------------");
		} else {
			app_name = intent.getStringExtra("app_name");
			file_name = app_name + ".apk";
			patchUrl = patchUrl + app_name + ".patch";
			// 创建文件
			CommonUtil.createFile(app_name + ".patch");
			CommonUtil.createFile(file_name);
			updateFile = new File(updateDir + "/" + app_name + ".patch");
			if(flag==1)flag=2;
			broadCast = new Intent();
			broadCast.setAction(DownLoadApkUtil.DOWNLOAD_ACTION);
			broadCast.putExtra("download", 0);
			sendBroadcast(broadCast);
			downFile();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/***
	 * 开线程下载
	 */
	public void createThread(final String downUrl) {
		final Message message = new Message();
		if (CommonUtil.isNetworkAvailable(getApplicationContext())) {
			new Thread(() -> {
				try {
					OkHttpUtil.getInstance().download(downUrl,0, newApkUrl, file_name, new OkHttpUtil.OnDownloadListener() {
						@Override
						public void onDownloadSuccess(File downfile, File file) {
							Log.i("wsy",file.getName()+"下载完成");
							hand();
							stopSelf();
						}
						@Override
						public void onDownloading(int progress, File file) {
							broadCast.putExtra("download", progress);
							sendBroadcast(broadCast);
						}
						@Override
						public void onDownloadFailed(String error) {
							Log.e("wsy","下载错误"+error);
						}
					});
				} catch (Exception e) {
				}
			}).start();
		} else {
			Toast.makeText(getApplicationContext(), "网络无连接，请稍后下载！",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 根据类型下载不同的文件
	 *
	 * @exception
	 * @since 1.0.0
	 */
	protected void downFile() {
		// 下载增量包路径
		if (1 == flag) {
//			downUrl = (String) vsPreference.getData(Constant.VERSION_PATCH_FILE_PATH,"");
		} else if (2 == flag) {
			downUrl =DownLoadApkUtil.Custom_DOWNLOADAPK_URL;
		}
		createThread(downUrl);
	}

	/**
	 * 根据不同的包安装
	 *
	 * @exception
	 * @since 1.0.0
	 */
	protected void hand() {
		// 增量合成方法
		if (1 == flag) {
		}
		// 整包安装方法
		else if (2 == flag) {
			Log.i("wsy","开始安装");
			File apkFile = new File(newApkUrl, file_name);
			OkHttpUtil.getInstance().openFile(apkFile,this);
		}
	}
}
