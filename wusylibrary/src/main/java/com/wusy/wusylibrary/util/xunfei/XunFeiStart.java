package com.wusy.wusylibrary.util.xunfei;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by TJ on 2018/8/29.
 */

public class XunFeiStart {
    private Toast mToast;
    private boolean mTranslateEnable = false;
    private SharedPreferences mSharedPreferences;
    private SpeechRecognizer mIat;
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    private Context mContext;
    private static volatile XunFeiStart xunFeiStart;
    private onChangeListener changeListener;


    private XunFeiStart(Context context){

        SpeechUtility.createUtility(context, SpeechConstant.APPID +"=5b975fc4");
        InitListener mInitListener = new InitListener() {

            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    showTip("初始化失败，错误码：" + code);
                }
            }
        };
        mIat=SpeechRecognizer.createRecognizer(context, mInitListener);
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mSharedPreferences = context.getSharedPreferences("com.iflytek.setting",
                Activity.MODE_PRIVATE);
        mContext = context;}

    public static XunFeiStart getInstance(Context context){
        if (xunFeiStart==null){
            synchronized (XunFeiStart.class){
                if (xunFeiStart==null){
                    xunFeiStart = new XunFeiStart(context);
                }
            }
        }
        return xunFeiStart;
    }

    int ret = 0; // 函数调用返回值
    //开始
    public void start(String path){

        if( null == mIat ){
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip( "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化" );
            return ;
        }

        FlowerCollector.onEvent(mContext, "iat_recognize");
        mIatResults.clear();
        // 设置参数
        setParam(path);
        // 不显示听写对话框
        ret = mIat.startListening(mRecognizerListener);
//        if (ret != ErrorCode.SUCCESS) {
////            showTip("听写失败,错误码：" + ret);
//        } else {
//            showTip("请开始说话…");
//        }
    }

    public void stop(){
        mIat.stopListening();
    }

    /**
     * 参数设置
     *
     * @return 传入录音文件需要存储的全路径加名字加后缀 必须是wav格式
     */
    private void setParam(String path) {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        String mEngineType = SpeechConstant.TYPE_CLOUD;
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean("translate",false);
        if( mTranslateEnable ){
            mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
            mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
            mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
            }
        }
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, path);//设置录音文件存储路径
    }


    private void showTip(final String str) {
//        if (str.startsWith("您好像没有说话") ) {
//            return;
//        }
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        String re ="";

        @Override
        public void onBeginOfSpeech() {
//            setMyResult("");
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            textView.setText("");
//            showTip("开始说话");
            //显示自定义的dialog
        }

        @Override
        public void onError(SpeechError error) {
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
//                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
//            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {

            if( mTranslateEnable ){
                printTransResult( results );
            }else{
                re = getResult(results);
            }

            if (isLast) {
                changeListener.onChange(re);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }


    };

    private void printTransResult (RecognizerResult results) {
        String trans  = JsonParser.parseTransResult(results.getResultString(),"dst");
        String oris = JsonParser.parseTransResult(results.getResultString(),"src");

        if( TextUtils.isEmpty(trans)||TextUtils.isEmpty(oris) ){
            showTip( "解析结果失败，请确认是否已开通翻译功能。" );
        }else{
//            mResultText.setText( "原始语言:\n"+oris+"\n目标语言:\n"+trans );
        }

    }

    private String getResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        return resultBuffer.toString();
    }

    public interface onChangeListener{
        void onChange(String value);
    }

    public void setOnChangeListener(onChangeListener onChangeListener){
        this.changeListener = onChangeListener;
    }



}
