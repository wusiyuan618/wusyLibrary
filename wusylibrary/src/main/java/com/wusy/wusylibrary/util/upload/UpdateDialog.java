package com.wusy.wusylibrary.util.upload;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class UpdateDialog extends Dialog {

    Context context;
    public UpdateDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public UpdateDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        this.setContentView(R.layout.upgrade_dialog);
    }
}
