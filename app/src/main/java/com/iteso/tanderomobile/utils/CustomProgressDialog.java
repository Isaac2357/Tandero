package com.iteso.tanderomobile.utils;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.iteso.tanderomobile.R;

public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.custom_progress_dialog);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
