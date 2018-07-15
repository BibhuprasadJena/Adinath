package com.release.eztoll.Util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.release.eztoll.R;

/**
 * Created by BIBHU.
 */


public class ProgressHandler extends Dialog {

    public ProgressHandler(Context mContext) {
        super(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.progress_handler, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        getWindow().setDimAmount(0f);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setDimAmount(0f);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }

    public ProgressHandler(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public boolean isShowing() {
        return super.isShowing();
    }


    public void hide() {
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}

