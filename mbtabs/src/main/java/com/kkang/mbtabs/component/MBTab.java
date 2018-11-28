package com.kkang.mbtabs.component;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkang.mbtabs.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MBTab extends LinearLayout {
    public static void Debug(String msg) {
        Log.d("KKANG", buildLogMsg(msg));
    }

    private static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName());
        sb.append(" > ");
        sb.append(ste.getMethodName());
        sb.append(" > #");
        sb.append(ste.getLineNumber());
        sb.append("] ");
        sb.append(message);

        return sb.toString();
    }

    public MBTabBar tabBar;
    public ViewPager viewPager;
    private LinearLayout llTab;
    private ImageView ivFading;
    public ImageView ivAll;
    private TextView tvAll;
    private LinearLayout llAllBtn;
    private ArrayList<CUSTOM_TAB_DATA> tabList;
    private setTabClickListener listener;

    private boolean isOpen = false;

    public MBTab(Context context) {
        super(context);
        tabList = new ArrayList<>();
        init();
    }

    public MBTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MBTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.view_mb_tab, this);

        setUI();
    }

    public void setData(ArrayList<CUSTOM_TAB_DATA> tabList, boolean isAllBtn) {
        this.tabList = tabList;
        if (!isAllBtn) {
            llAllBtn.setVisibility(View.GONE);
            ivFading.setVisibility(View.GONE);
            tvAll.setVisibility(View.GONE);
        }
        updateView();
        setEvent();
    }

    public void setAllBtnState(boolean isOpen){
        this.isOpen = isOpen;
        if (isOpen) {
            ivAll.setImageResource(R.drawable.sketch_cabtn_up_180927);
            tvAll.setVisibility(View.VISIBLE);
            ivFading.setVisibility(View.GONE);
            UtilAnim.fideOut(tabBar, 100, null);
        } else {
            ivAll.setImageResource(R.drawable.sketch_cabtn_down_180927);
            tvAll.setVisibility(View.GONE);
            ivFading.setVisibility(View.VISIBLE);
            UtilAnim.fideIn(tabBar, 100, null);
        }
    }

    private void setUI() {
        tabBar = (MBTabBar) findViewById(R.id.tabBar);
        llTab = (LinearLayout) findViewById(R.id.llTab);
        ivFading = (ImageView) findViewById(R.id.ivFading);
        ivAll = (ImageView) findViewById(R.id.ivAll);
        tvAll = (TextView) findViewById(R.id.tvAll);
        llAllBtn = (LinearLayout) findViewById(R.id.llAllBtn);
    }

    private void setEvent() {
        tabBar.setOnTabSelectedListener(new MBTabBar.setOnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (listener != null) {
                    listener.onTabClickListener(position);
                }
            }

            @Override
            public void onScrollEnd() {
                ivFading.setVisibility(View.GONE);
            }

            @Override
            public void onScrolling() {
                ivFading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAllBtnShow(boolean isShow) {
                if (isShow) {
                    llAllBtn.setVisibility(View.VISIBLE);
                } else {
                    llAllBtn.setVisibility(View.GONE);
                }
            }
        });

        ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    isOpen = !isOpen;
                    setAllBtnState(isOpen);
                    listener.onAllClickListener(isOpen);
                }
            }
        });
    }

    private void updateView() {
        tvAll.setVisibility(View.GONE);

        UtilAnim.fideIn(tabBar, 200, null);

        tabBar.setIndicatorHeight(5);
        tabBar.setBackgroundColor(Color.WHITE);
        tabBar.setData(tabList);
    }

    public void setListener(setTabClickListener listener) {
        this.listener = listener;
    }

    public void setCollectTabSetting(String title, String colorCode) {
        tvAll.setText(title);
        tvAll.setTextColor(Color.parseColor(colorCode));
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        if (tabBar != null) {
            tabBar.setViewPager(viewPager);
        }
    }

    public static int intToDp(Context context, int value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public static int floatToDp(Context context, float value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public interface setTabClickListener {
        void onTabClickListener(int position);

        void onAllClickListener(boolean isOpen);


    }
}