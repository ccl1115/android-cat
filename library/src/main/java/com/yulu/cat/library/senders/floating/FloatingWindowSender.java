package com.yulu.cat.library.senders.floating;

import android.content.Context;
import android.content.Intent;

import com.yulu.cat.library.LevelDef;
import com.yulu.cat.library.Sender;

import java.util.LinkedList;
import java.util.List;


/**
 * @author yulu
 */

public class FloatingWindowSender implements Sender {

    private final Context context;
    private final float alpha;
    private final int height;

    private List<String> cache;

    public FloatingWindowSender(Context context, float alpha, int height) {

        this.context = context;
        this.alpha = alpha;
        this.height = height;

        cache = new LinkedList<>();

        checkService();
    }

    @Override
    public String name() {
        return "floating";
    }

    @Override
    public void log(String tag, @LevelDef int level, String msg) {
        generalLog(tag, msg);
    }

    @Override
    public void v(String tag, String msg) {
        generalLog(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        generalLog(tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        generalLog(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        generalLog(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        generalLog(tag, msg);
    }

    private synchronized void generalLog(String tag, String msg) {
        if (FloatingViewService.instance != null) {
            for (String s : cache) {
                FloatingViewService.instance.insert(s);
            }
            FloatingViewService.instance.insert(String.format("[%s] %s", tag, msg));
        } else {
            cache.add(String.format("[%s] %s", tag, msg));
            checkService();
        }
    }

    private void checkService() {
        if (FloatingViewService.instance == null) {
            Intent intent = new Intent(context, FloatingViewService.class);
            intent.putExtra("alpha", alpha);
            intent.putExtra("height", height);
            context.startService(intent);
        }
    }
}
