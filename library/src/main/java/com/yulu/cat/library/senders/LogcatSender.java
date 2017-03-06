package com.yulu.cat.library.senders;

import android.util.Log;

import com.yulu.cat.library.Cat;
import com.yulu.cat.library.Sender;


/**
 * A sender that sends logs to Android logcat service
 * @author yulu
 */
public class LogcatSender implements Sender {
    @Override
    public String name() {
        return "logcat";
    }

    @Override
    public void log(String tag, int level, String msg) {
        switch (level) {
            case Cat.LEVEL_V:
                v(tag, msg);
                break;
            case Cat.LEVEL_I:
                i(tag, msg);
                break;
            case Cat.LEVEL_D:
                d(tag, msg);
                break;
            case Cat.LEVEL_W:
                w(tag, msg);
                break;
            case Cat.LEVEL_E:
                e(tag, msg);
            case Cat.LEVEL_ALL:
                break;
        }
    }

    @Override
    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}
