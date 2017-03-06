package com.yulu.cat.library.senders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.yulu.cat.library.Cat;
import com.yulu.cat.library.LevelDef;
import com.yulu.cat.library.Sender;


/**
 * ToastSender shows logs in Android toast
 */

public class ToastSender implements Sender {

    private static final SparseIntArray DEFAULT_COLOR = new SparseIntArray();
    private final Context context;
    private final boolean singleToast;
    private Toast toast;

    static {
        DEFAULT_COLOR.append(Cat.LEVEL_V, Color.GRAY);
        DEFAULT_COLOR.append(Cat.LEVEL_I, Color.GREEN);
        DEFAULT_COLOR.append(Cat.LEVEL_D, Color.BLUE);
        DEFAULT_COLOR.append(Cat.LEVEL_W, Color.YELLOW);
        DEFAULT_COLOR.append(Cat.LEVEL_E, Color.RED);
    }

    public ToastSender(Context context, boolean singleToast) {
        this.context = context;
        this.singleToast = singleToast;
    }

    @Override
    public String name() {
        return "toast";
    }

    @Override
    public void log(String tag, @LevelDef int level, String msg) {
        getToast(tag, level, msg).show();
    }

    @Override
    public void v(String tag, String msg) {
        getToast(tag, Cat.LEVEL_V, msg).show();
    }

    @Override
    public void i(String tag, String msg) {
        getToast(tag, Cat.LEVEL_I, msg).show();
    }

    @Override
    public void d(String tag, String msg) {
        getToast(tag, Cat.LEVEL_D, msg).show();
    }

    @Override
    public void w(String tag, String msg) {
        getToast(tag, Cat.LEVEL_W, msg).show();
    }

    @Override
    public void e(String tag, String msg) {
        getToast(tag, Cat.LEVEL_E, msg).show();
    }

    @SuppressLint("ShowToast")
    private Toast getToast(String tag, @LevelDef int level, String msg) {
        if (singleToast) {
            if (toast != null) {
                toast.cancel();
            }
        }
        toast = Toast.makeText(context, String.format("<%s> %s", tag, msg), Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(DEFAULT_COLOR.get(level));
        return toast;
    }
}
