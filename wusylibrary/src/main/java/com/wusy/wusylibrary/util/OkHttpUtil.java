package com.wusy.wusylibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.HttpException;


/**
 * Created by 达拉然的风 on 2017/5/8.
 */

public class OkHttpUtil {
    public static OkHttpUtil okHttpUtil;
    public final String LOGTAG = "OkHttpMsg";
    private OkHttpClient mOkHttpClient;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    public  long progress=0;
    public static final int SEND=0;

    private OkHttpUtil() {
        init();
    }

    public synchronized static OkHttpUtil getInstance() {
        if (okHttpUtil == null) {
            okHttpUtil = new OkHttpUtil();
        }
        return okHttpUtil;
    }

    private void init() {
        //2018-5-9增加了超时限制 wusy
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);//设置日志显示级别
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(20000, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();


    }

    /**
     * 这是一个利用OKHttp做get请求的方法
     * 由于okhttp的网络请求并不在UI线程上。利用回调来实现
     *
     * @param url      网络请求的url
     * @param activity
     * @param callback 请求结果回调
     */
    public void asynGet(String url, final Activity activity, HashMap<String,String> headers,
                        final ResultCallBack callback) {
        showLog("正在进行Get请求，url：" + url);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("Authorization","admin");

        if(headers!=null){
            for (String key : headers.keySet()) {
                Log.i("wsy","key="+key+"\nvalue="+headers.get(key));
                builder=builder.addHeader(key,headers.get(key));
            }
        }
        final Request request=builder.build();
        deliveryResult(callback, request, activity);
    }

    public String syncGet(String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token","")
                .build();
        Call call = mOkHttpClient.newCall(request);
        String result = "";
        try {
            result = call.execute().body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void asynGet(String url, HashMap<String,String> headers,final ResultCallBack callback) {
        asynGet(url,null,headers,callback);
    }
    public void asynGet(String url, final ResultCallBack callback) {
        asynGet(url,null,null,callback);
    }

    /**
     * 这是一个利用OKHttp做POST请求的方法
     * 由于okhttp的网络请求并不在UI线程上。利用回调来实现
     *
     * @param url      网络请求的url
     * @param maps     Post上传的参数集合
     * @param activity
     * @param callback 请求结果回调
     */
    public void asynPost(String url, final Activity activity,String token ,Map<String, String> maps,
                         final ResultCallBack callback) {
        showLog("正在进行Post请求，url：" + url + "\n上传的值是：" + maps.toString());
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, String> map : maps.entrySet()) {
            formBody.add(map.getKey(), map.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .post(formBody.build())
                .build();
        deliveryResult(callback, request, activity);
    }

    /**
     * Post直接提交JSON字符串
     */
    public void anysPost(String url,String token,String json,final ResultCallBack callback){
        showLog("正在进行Post请求，url：" + url + "\n上传的值是：" + json);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .post(requestBody)
                .build();
        deliveryResult(callback, request, null);
    }
    public void asynPost(String url,String token, Map<String, String> maps,
                         final ResultCallBack callback) {
        asynPost(url,null,token,maps,callback);
    }
    public void asynPost(String url, Map<String, String> maps,
                         final ResultCallBack callback) {
        asynPost(url,null,"",maps,callback);
    }
    /**
     * 单文件上传
     */
    public void upLoadFile(String url, String fileIndex,File file, Map<String, String> params, ResultCallBack callback){
        showLog("正在进行文件上传，url：" + url);
        /* form的分割线,自己定义 */
        String boundary = "xx--------------------------------------------------------------xx";
        MultipartBody.Builder mBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        mBody.addFormDataPart(fileIndex, file.getName(), fileBody);
        for (Map.Entry<String, String> param : params.entrySet()) {
            mBody.addFormDataPart(param.getKey(), param.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(mBody.build())
                .build();
        deliveryResult(callback, request, null);
    }
    /**
     * 多文件上传
     *
     * @param url      文件上传的url
     * @param files    上传的文件
     * @param callback 结果的回调函数
     * @param activity
     */
    public void upLoadFile(String url, final List<File> files, Map<String, String> params, ResultCallBack callback, final Activity activity) {
        /* form的分割线,自己定义 */
        String boundary = "xx--------------------------------------------------------------xx";



        MultipartBody.Builder mBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);

        for (int i=0;i<files.size();i++){
            progress = 0;
//            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), files.get(i));
            RequestBody fileBody =createCustomRequestBody(MediaType.parse("application/octet-stream"), files.get(i), new ProgressListener() {
                @Override
                public void onProgress(long totalBytes, long remainingBytes, boolean done) {

                    progress = (totalBytes-remainingBytes)*100/totalBytes;

                }
            });
            mBody.addFormDataPart(("file"+i), files.get(i).getName(), fileBody);
        }
        for (Map.Entry<String, String> param : params.entrySet()) {
            mBody.addFormDataPart(param.getKey(), param.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(mBody.build())
                .build();
        deliveryResult(callback, request, activity);
    }
    public void upLoadFile(String url, List<File> files, Map<String, String> params, ResultCallBack callback) {
        upLoadFile(url,files,params,callback,null);
    }

    private static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {


            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {

                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
    }


    interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }



    /**
     * 文件下载方法
     * @param url
     * @param fileSize 下载文件的大小，无法获取就传0
     * @param saveDir
     * @param fileName
     * @param listener
     */
    public void download(final String url, final long fileSize, final String saveDir, final String fileName, final OnDownloadListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept-Encoding","identity")
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                File file = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total=1;
                    if(fileSize>0) total=fileSize;
                    else total = response.body().contentLength();
                    Log.i(LOGTAG, "savePath---" + saveDir + "\tfileName---" + fileName);
                    file = new File(savePath, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress, file);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess(file, file);
                } catch (Exception e) {
                    listener.onDownloadFailed(e.getLocalizedMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
    /**
     * 判断文件是否存在
     * @param file
     */
    public void judeFileExists(File file) {

        if (file.exists()) {
            Logger.i("file exists");
        } else {
            Logger.i("file not exists, create it ...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 判断文件夹是否存在
     * @param file
     */
    public void judeDirExists(File file) {

        if (file.exists()) {
            if (file.isDirectory()) {
                Logger.i("dir exists");
            } else {
                Logger.i("the same name file exists, can not create dir");
            }
        } else {
            Logger.i("dir not exists, create it ...");
            file.mkdir();
        }

    }
    /**
     * 判断下载目录是否存在
     * @param saveDir 保存路径
     * @return
     * @throws IOException
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 后台打印Log信息
     *
     * @param msg 需要打印的内容
     */
    private void showLog(String msg) {
        Logger.i(msg);
    }

    /**
     * 创建Call开始异步请求
     *
     * @param callback 请求结果回调
     * @param request  请求体
     * @param activity
     */
    private void deliveryResult(final ResultCallBack callback, final Request request, final Activity activity) {

            Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                String message="";
                Logger.e( "请求失败，错误信息：" + e.getLocalizedMessage());
                if (e instanceof SocketTimeoutException) {
                    message = "网络连接超时";
                } else if (e instanceof ConnectException) {
                    message = "连接失败";
                } else if (e instanceof IOException) {
                    message = "网络错误";
                } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
                    message = "证书验证失败";
                } else {
                    message = "未知错误";
                }
                if(activity!=null) {
                    String finalMessage = message;
                    activity.runOnUiThread(() -> {
                        callback.failListener(call, e, finalMessage);
                    });
                }else{
                    Logger.e( "请求失败，错误信息：" + e.getLocalizedMessage());
                    callback.failListener(call, e,message);
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
//                final String responseStr=response.body().string();

                if(activity!=null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.successListener(call,response);
                        }
                    });
                }else{
                    callback.successListener(call,response);
                }
            }
        });
    }

    /**
     * 判断文件是否存在
     * @param f
     * @return
     */
    public boolean isExists(File f){
        try
        {
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 打开附件的方法
     * @param f
     * @param context
     */
    public void openFile(File f, Context context) {
        try {
            Log.i("msg",f.getName()+"--"+f.length());
            String end = f.getName().substring(f.getName().lastIndexOf(".")
                    + 1, f.getName().length()).toLowerCase();
            if(end.equals("amr")||end.equals("wav")){
                AudioRecoderUtils.getInstance().playerStart(f.getAbsolutePath());
            }else if(end.equals("pcm")){
                String pcmPath=f.getPath();
                String wavPath=f.getPath().replace("pcm","wav");
                AudioRecoderUtils audioRecoderUtils=AudioRecoderUtils.getInstance();
                audioRecoderUtils.convertPcmToWav(pcmPath,wavPath,15000,1,8);
                audioRecoderUtils.playerStart(wavPath);
            } else {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.addCategory("android.intent.category.DEFAULT");
      /* 调用getMIMEType()来取得MimeType */
                String type = getMIMEType(f);
      /* 设置intent的file与MimeType */
                if(Build.VERSION.SDK_INT>=24){
                    Uri contenturi=FileProvider.getUriForFile(context, "com.wusy.fileprovider",f);
                    intent.setDataAndType(contenturi,type);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contenturi);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }else{
                    intent.setDataAndType(Uri.fromFile(f), type);
                }
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(context,"打开附件---"+f.getName()+"，发生了错误",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     *  判断文件MimeType的method
     */
    private String getMIMEType(File f)
    {
        String type;
        String fName=f.getName();
      /* 取得扩展名 */
        String end=fName.substring(fName.lastIndexOf(".")
                +1,fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */

        switch (end){
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                type="audio";
                break;
            case "3gp":
            case "mp4":
                type="video";
                break;
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                type="image";
                break;
            case "apk":
                type = "application/vnd.android.package-archive";
                break;
            case "pdf":
                type="application/pdf";
                break;
            case "xls":
            case "xlsx":
                type="application/ms-excel";
                break;
            case "doc":
            case "docx":
                type="application/msword";
                break;
            case "ppt":
                type="application/vnd.ms-powerpoint";
                break;
                default:
                    type="*/*";
                    break;
        }
        return type;
    }

    /**
     * get、post请求回调
     */
    public interface ResultCallBack {
        void successListener(Call call,Response response);

        void failListener(Call call, IOException e,String message);
    }

    /**
     * 文件下载监听
     */
    public interface OnDownloadListener {
        /**
         * 下载成功
         *
         * @param downfile 下载的文件
         */
        void onDownloadSuccess(File downfile, File file);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress, File file);

        /**
         * 下载失败
         */
        void onDownloadFailed(String error);
    }
}
