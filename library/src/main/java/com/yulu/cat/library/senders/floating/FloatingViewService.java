package com.yulu.cat.library.senders.floating;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yulu.cat.library.LevelDef;
import com.yulu.cat.library.R;

/**
 * @author yulu
 */

public class FloatingViewService extends Service {

    static FloatingViewService instance;

    private FloatingViewPresenter presenter;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final float alpha = intent.getFloatExtra("alpha", .5f);
        final int height = intent.getIntExtra("height",
                getResources().getDimensionPixelSize(R.dimen.default_floating_view_height));
        presenter = new FloatingViewPresenter(this, alpha, height);
        presenter.init();
        instance = this;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        presenter.deinit();
        instance = null;
        super.onDestroy();
    }

    void insert(@LevelDef int level, String tag, String msg) {
        presenter.insert(tag, msg, level);
    }
}
