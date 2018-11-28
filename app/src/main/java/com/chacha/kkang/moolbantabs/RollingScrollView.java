package com.chacha.kkang.moolbantabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class RollingScrollView extends ScrollView {
    LinearLayout layoutContainer;
    ArrayList<TALK_NOTI_LIST> list;

    int llHeight = 0;

    int childPosition = 0;

    int itemHeight = 0;

    int scrollOffset = 0;

    Runnable runnable;
    ObjectAnimator oa;
    AnimatorListenerAdapter animatorListenerAdapter;

    public RollingScrollView(Context context) {
        super(context);
        init();
    }

    public RollingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RollingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFillViewport(true);
        setVerticalScrollBarEnabled(false);
        layoutContainer = new LinearLayout(getContext());

        layoutContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutContainer.setOrientation(LinearLayout.VERTICAL);
        addView(layoutContainer);
        runnable = new Runnable() {
            @Override
            public void run() {
                scrollOffset += itemHeight;

                oa = ObjectAnimator.ofInt(RollingScrollView.this, "scrollY", scrollOffset);
                oa.addListener(animatorListenerAdapter);
                oa.setDuration(1000).start();

            }
        };
        animatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                addView();
            }
        };
    }

    public void setData(ArrayList<TALK_NOTI_LIST> list) {
        this.list = list;
        final ItemTalkNoti item = new ItemTalkNoti(getContext());
        layoutContainer.addView(item);
        item.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                item.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d("KLL", "item.getH  " + item.getHeight());
                itemHeight = item.getHeight();
                resetUI();
                updateUI();
            }
        });

    }

    private void resetUI() {
        layoutContainer.removeAllViews();
        childPosition = 0;
        scrollTo(0, 0);
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        if (oa != null) {
            oa.removeListener(animatorListenerAdapter);
            oa.cancel();
        }
        scrollOffset = 0;
    }

    private void updateUI() {
        for (int i = 0; i < list.size(); i++) {
            RelativeLayout rl = new RelativeLayout(getContext());
            layoutContainer.addView(rl);
            LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams) rl.getLayoutParams();
            rlParams.height = itemHeight;
            rlParams.width = MATCH_PARENT;
            rl.setLayoutParams(rlParams);

            ItemTalkNoti item = new ItemTalkNoti(getContext());
            item.setData(list.get(i), null);
            rl.addView(item);

            RelativeLayout.LayoutParams itemParams = (RelativeLayout.LayoutParams) item.getLayoutParams();
            itemParams.height = MATCH_PARENT;
            itemParams.width = MATCH_PARENT;
            item.setLayoutParams(itemParams);
            llHeight += itemHeight;
        }

        if (list.size() > 1) {
            movePostion();
        }
    }

    private void addView() {
        ScrollView.LayoutParams params = (ScrollView.LayoutParams) layoutContainer.getLayoutParams();
        llHeight += itemHeight;
        params.height = llHeight;
        layoutContainer.setLayoutParams(params);

        if (layoutContainer.getChildCount() > childPosition) {
            ViewGroup vg = (ViewGroup) layoutContainer.getChildAt(childPosition);
            if (vg.getChildCount() > 0) {
                View v = vg.getChildAt(0);
                vg.removeViewAt(0);

                RelativeLayout rl = new RelativeLayout(getContext());
                layoutContainer.addView(rl);
                LinearLayout.LayoutParams rlParams = (LinearLayout.LayoutParams) rl.getLayoutParams();
                rlParams.height = itemHeight;
                rlParams.width = MATCH_PARENT;
                rl.setLayoutParams(rlParams);
                rl.addView(v);
                childPosition += 1;

                movePostion();
            }
        }
    }

    private void movePostion() {
        postDelayed(runnable, 1500);

    }
}

