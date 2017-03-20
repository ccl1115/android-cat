package com.yulu.cat.library.senders.floating;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yulu.cat.library.Cat;
import com.yulu.cat.library.LevelDef;
import com.yulu.cat.library.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

/**
 * Presenter for Memory info view
 *
 * @author yulu
 */
class FloatingViewPresenter implements View.OnClickListener {

    private Context mContext;
    private float mAlpha;
    private int mHeight;

    private View mRoot;

    private RecyclerView mRvContent;
    private ImageView mIvFullscreen;
    private ImageView mIvClose;

    private ViewPropertyAnimator mLlInfoViewAnimate;
    private View.OnClickListener mOuterOnClickListener;
    private LogAdapter adapter;

    private boolean fullscreen;

    @SuppressWarnings("deprecation")
    FloatingViewPresenter(Context context, float alpha, int height) {
        mContext = context;
        mAlpha = alpha;
        mHeight = height;
    }

    void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Toast.makeText(mContext, R.string.no_permission, Toast.LENGTH_LONG).show();
                return;
            }
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                MATCH_PARENT,
                mHeight,
                TYPE_SYSTEM_ALERT,
                FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCH_MODAL | FLAG_LAYOUT_NO_LIMITS
                        | FLAG_LAYOUT_INSET_DECOR | FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;

        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mRoot = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.widget_info, null);
        mRoot.setAlpha(mAlpha);
        mRvContent = (RecyclerView) mRoot.findViewById(R.id.rv_content);
        mIvFullscreen = (ImageView) mRoot.findViewById(R.id.iv_fullscreen);
        mIvClose = (ImageView) mRoot.findViewById(R.id.iv_close);
        mIvFullscreen.setOnClickListener(this);
        mIvClose.setOnClickListener(this);

        adapter = new LogAdapter();
        mRvContent.setAdapter(adapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        manager.addView(mRoot, params);
    }

    void deinit() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        manager.removeView(mRoot);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_fullscreen) {
            toggleFullscreen();
        } else if (i == R.id.iv_close) {
            mRoot.setVisibility(View.GONE);
        }
    }

    private void toggleFullscreen() {
        ViewGroup.LayoutParams layoutParams = mRoot.getLayoutParams();
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (fullscreen) {
            layoutParams.height = mHeight;
            manager.updateViewLayout(mRoot, layoutParams);
            mRvContent.scrollToPosition(adapter.getItemCount() - 1);
            fullscreen = false;
        } else {
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            manager.updateViewLayout(mRoot, layoutParams);
            fullscreen = true;
        }
    }

    void insert(String tag, String log, @LevelDef int level) {
        if (adapter != null) {
            adapter.insert(tag, log, level);
            mRvContent.smoothScrollToPosition(adapter.getItemCount() - 1);
            mRoot.setVisibility(View.VISIBLE);
        }
    }


    private static class LogAdapter extends RecyclerView.Adapter<LogItemViewHolder> {
        private String lastTag;

        private void insert(String tag, String msg, @LevelDef int level) {
            if (data.size() > 100) {
                data.remove(0);
            }
            if (tag.equals(lastTag)) {
                data.add(new Item(level, "", msg));
            } else {
                data.add(new Item(level, tag, msg));
            }
            lastTag = tag;
        }

        private List<Item> data = new ArrayList<Item>() {
            @Override
            public boolean add(Item o) {
                boolean add = super.add(o);
                notifyItemInserted(size() - 1);
                return add;
            }

            @Override
            public Item remove(int index) {
                Item remove = super.remove(index);
                notifyItemRemoved(index);
                return remove;
            }
        };

        @Override
        public LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_log, parent, false);
            return new LogItemViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(LogItemViewHolder holder, int position) {
            Item item = data.get(position);
            holder.mTvTag.setText(item.tag);
            holder.mTvMsg.setText(item.msg);
            holder.mTvTag.setBackgroundColor(item.tag.hashCode() | 0xFF000000);
           switch (item.level) {
                case Cat.LEVEL_ALL:
                    break;
                case Cat.LEVEL_D:
                    holder.mTvMsg.setBackgroundColor(Color.BLUE);
                    break;
                case Cat.LEVEL_E:
                    holder.mTvMsg.setBackgroundColor(Color.RED);
                    break;
                case Cat.LEVEL_I:
                    holder.mTvMsg.setBackgroundColor(Color.CYAN);
                    break;
                case Cat.LEVEL_V:
                    holder.mTvMsg.setBackgroundColor(Color.GRAY);
                    break;
                case Cat.LEVEL_W:
                    holder.mTvMsg.setBackgroundColor(Color.YELLOW);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private static class LogItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvTag;
        private TextView mTvMsg;

        LogItemViewHolder(View itemView) {
            super(itemView);
            mTvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
        }
    }

    private static class Item {
        @LevelDef
        int level;
        String msg;
        String tag;

        Item(@LevelDef int level, String tag, String msg) {
            this.level = level;
            this.msg = msg;
            this.tag = tag;

        }
    }
}
