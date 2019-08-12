package com.wusy.wusylibrary.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.wusy.wusylibrary.R;

import java.util.ArrayList;

/**
 * Created by DalaR on 2017/12/21.
 */

public class MaterialDialogsUtil {
    private static MaterialDialogsUtil materialDialogsUtil;
    private MaterialDialog DialogPro;
    private static Context mC;

    private MaterialDialogsUtil(){
    }
    public synchronized static MaterialDialogsUtil getInstance(Context context){
        MaterialDialogsUtil.mC=context;
        if(materialDialogsUtil==null){
            materialDialogsUtil=new MaterialDialogsUtil();
        }
        return materialDialogsUtil;
    }
    /**
     * 构建一个确认窗口
     */
    public MaterialDialog.Builder createMakeSureDialog(){
        return new MaterialDialog.Builder(mC)
                .content("一个简单的dialog,高度会随着内容改变")
                .positiveText("同意")
                .negativeText("不同意")
                .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色
                .checkBoxPrompt("不再提醒", false, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Toast.makeText(mC, "不再提醒", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mC, "会再次提醒", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            Toast.makeText(mC, "同意", Toast.LENGTH_LONG).show();
                        } else if (which == DialogAction.NEGATIVE) {
                            Toast.makeText(mC, "不同意", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(mC, "else", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 构建一个简单的Dialog。
     * @return
     */
    public MaterialDialog.Builder createSimpleDialog(){
        return new MaterialDialog.Builder(mC)
                .title("simple dialog")
                .content("一个简单的dialog,高度会随着内容改变")
                .iconRes(R.mipmap.ic_launcher)
                .positiveText("同意")
                .negativeText("不同意")
                .neutralText("更多信息")
                .autoDismiss(true)//不自动消失
                .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色
                .checkBoxPrompt("不再提醒", false, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Toast.makeText(mC, "不再提醒", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mC, "会再次提醒", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.NEUTRAL) {
                            Toast.makeText(mC, "更多信息", Toast.LENGTH_LONG).show();
                        } else if (which == DialogAction.POSITIVE) {
                            Toast.makeText(mC, "同意", Toast.LENGTH_LONG).show();
                        } else if (which == DialogAction.NEGATIVE) {
                            Toast.makeText(mC, "不同意", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public MaterialDialog.Builder createDanXDialog() {
        ArrayList<String> list = new ArrayList<>();
        list.add("aaaaaaaaaaa");
        list.add("bbbbbbbbbbb");
        list.add("ccccccccccc");
        list.add("ddddddddddd");
        list.add("fffffffffff");
        list.add("eeeeeeeeeee");
        list.add("ggggggggggg");
        return new MaterialDialog.Builder(mC)
                .title("List Dialog")
                .iconRes(R.mipmap.ic_launcher)
                .content("List Dialog,显示数组信息，高度会随着内容扩大")
                .items(list)
                .positiveText("同意")
                .negativeText("不同意")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            Toast.makeText(mC, "同意", Toast.LENGTH_LONG).show();
                        } else if (which == DialogAction.NEGATIVE) {
                            Toast.makeText(mC, "不同意", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {//0 表示第一个选中 -1 不选
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        Logger.i("点击的是+" + text + "position是" + which);
                        return true;
                    }
                });
    }
    public MaterialDialog.Builder createDuoXDialog() {
        ArrayList<String> list = new ArrayList<>();
        list.add("aaaaaaaaaaa");
        list.add("bbbbbbbbbbb");
        list.add("ccccccccccc");
        list.add("ddddddddddd");
        list.add("fffffffffff");
        list.add("eeeeeeeeeee");
        list.add("ggggggggggg");
        return new MaterialDialog.Builder(mC)
                .title("Multi Choice List Dialogs")
                .iconRes(R.mipmap.ic_launcher)
                .content("Multi Choice List Dialogs,显示数组信息，高度会随着内容扩大.可以多选")
                .items(list)
                .positiveText("确定")
                .widgetColor(Color.RED)//改变checkbox的颜色
                //多选框添加
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        for (CharSequence a : text) {
                            Logger.i( "选中了" + a);
                        }
                        return true;//false 的时候没有选中样式
                    }
                })
                //点击确定后获取选中的下标数组
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Toast.makeText(mC, "选中" + dialog.getSelectedIndices().length + "个", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public MaterialDialog.Builder createInputDialog() {
        return new MaterialDialog.Builder(mC)
                .title("输入窗")
                .iconRes(R.mipmap.ic_launcher)
                .content("包含输入框的diaolog")
//               .widgetColor(Color.BLUE)//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_PHONE)//可以输入的类型-电话号码
                //前2个一个是hint一个是预输入的文字
                .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Logger.i( "输入的是：" + input);
                    }
                });
    }
    public MaterialDialog.Builder createProgrossDialog() {
        return new MaterialDialog.Builder(mC)
                .title("线型Progross")
                .content("等待动画")
                .autoDismiss(false)
                .progress(false, 100,true);
    }
    public MaterialDialog.Builder createLoadDialog() {
        return new MaterialDialog.Builder(mC)
                .title("圆形Progross")
                .content("等待动画")
                .autoDismiss(false)
                .progress(true, 100);
    }
}
