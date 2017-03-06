package com.yulu.cat;

import android.app.Application;

import com.yulu.cat.library.Cat;
import com.yulu.cat.library.Configuration;
import com.yulu.cat.library.Router;
import com.yulu.cat.library.senders.LogcatSender;
import com.yulu.cat.library.senders.ToastSender;
import com.yulu.cat.library.senders.floating.FloatingWindowSender;

/**
 * @author yulu
 */

public final class App extends Application {

    @Override
    public void onCreate() {
        Cat.builder()
                .context(this)
                .tag("toast")
                .tag("floating")
                .sender(new ToastSender(this, true))
                .sender(new LogcatSender())
                .sender(new FloatingWindowSender(this, 0.5f, 500))
                .router(new Router.Builder()
                        .add("toast", "toast")
                        .add("floating", "floating")
                        .add(Cat.LEVEL_E, "logcat")
                        .build())
                .build();


        super.onCreate();
    }

}
