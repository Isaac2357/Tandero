package com.iteso.tanderomobile.utils.ui;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.iteso.tanderomobile.R;

/**
 * CLASE .
 */
public class CustomProgressDialog extends Dialog {
    /**
     *
     * @param context .
     */
    public CustomProgressDialog(final @NonNull Context context) {
        super(context);
        setContentView(R.layout.custom_progress_dialog);
    }

    @Override
    public void cancel() {

    }

    @Override
    public final void show() {
        if (!isShowing()) {
            super.show();
        }
    }

    @Override
    public final void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
