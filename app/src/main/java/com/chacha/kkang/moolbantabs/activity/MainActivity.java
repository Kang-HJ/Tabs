package com.chacha.kkang.moolbantabs.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.adapter.Adapter_All;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Pager;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Sub;
import com.chacha.kkang.moolbantabs.component.MBTabBar;
import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.TAB_DATA;
import com.chacha.kkang.moolbantabs.UtilAnim;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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


    MBTabBar tabBar;
    RecyclerView rcvSub;
    ArrayList<TAB_DATA> tabList;
    TextView tvOnResumScroll;
    TextView tvAll;

    ImageView all;
    RecyclerView rcvAll;

    Adapter_All adapterAll;
    Adapter_Sub adapterSub;
    GridLayoutManager gridLayoutManager1;
    GridLayoutManager gridLayoutManager2;
    View back;

    ViewPager pager;
    Adapter_Pager adapterPager;

    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initData();
        setUI();
        setView();
        setEvent();
    }

    private void initData() {
        tabList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            TAB_DATA data = new TAB_DATA();
            data.key = i + "";
            data.name = i + " TAB";
            if (i == 3) {
                data.name += "BBBBB";
            }

            if (i == 0) {
                data.isSelect = true;
            }

            data.subList = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                TAB_DATA sub = new TAB_DATA();
                sub.key = i + "";
                sub.name = i + " SUB TAB";
                data.isSelect = false;
                data.subList.add(sub);
            }
            tabList.add(data);
        }

        adapterPager = new Adapter_Pager(this, tabList);

        gridLayoutManager1 = new GridLayoutManager(MainActivity.this, 3);
        gridLayoutManager2 = new GridLayoutManager(MainActivity.this, 3);


        adapterAll = new Adapter_All(this, tabList, new Adapter_All.setOnTabClickListener() {
            @Override
            public void onTabClick(int position, TAB_DATA data) {
                for (int i = 0; i < tabList.size(); i++) {
                    tabList.get(i).isSelect = false;
                }
                data.isSelect = true;
                adapterAll.notifyDataSetChanged();
            }

        });

        adapterSub = new Adapter_Sub(this, new Adapter_Sub.setOnSubTabClickListener() {
            @Override
            public void onSubTabClick(int position, TAB_DATA data) {
                if (tabList.size() > pager.getCurrentItem()) {
                    ArrayList<TAB_DATA> subList = tabList.get(pager.getCurrentItem()).subList;
                    for (int i = 0; i < subList.size(); i++) {
                        subList.get(i).isSelect = false;
                    }
                    data.isSelect = true;
                    adapterSub.notifyDataSetChanged();
                }

            }
        });
    }

    private void setUI() {
        back = (View) findViewById(R.id.back);
        tvOnResumScroll = (TextView) findViewById(R.id.tvOnResumScroll);
        tvAll = (TextView) findViewById(R.id.tvAll);
        all = (ImageView) findViewById(R.id.all);
        rcvAll = (RecyclerView) findViewById(R.id.rcvAll);
        pager = (ViewPager) findViewById(R.id.pager);

        tabBar = (MBTabBar) findViewById(R.id.tabBar);
        rcvSub = (RecyclerView) findViewById(R.id.rcvSub);


    }

    private void setView() {
        pager.setAdapter(adapterPager);
        pager.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        rcvAll.setLayoutManager(gridLayoutManager1);
        rcvAll.setAdapter(adapterAll);

        rcvAll.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        tvAll.setVisibility(View.GONE);

        UtilAnim.fideIn(tabBar, 200, null);

        rcvSub.setLayoutManager(gridLayoutManager2);
        rcvSub.setAdapter(adapterSub);

        tabBar.setDividerWidth(0);
        tabBar.setIndicatorHeight(5);
        tabBar.setBackgroundColor(Color.WHITE);

        tabBar.setIndicatorColor(getResources().getColor(R.color.tomato));
        ColorStateList colorList = createColorStateList(getResources().getColor(R.color.tomato)
                , getResources().getColor(R.color.tomato), Color.BLACK);

        tabBar.setTextColor(colorList);

        tabBar.setViewPager(pager);

    }

    private void setEvent() {
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    all.setImageResource(R.drawable.sketch_cabtn_up_180927);
                    back.setVisibility(View.GONE);
                    tvAll.setVisibility(View.GONE);
                    collapse(rcvAll);
                    UtilAnim.fideIn(tabBar, 100, null);
                } else {
                    all.setImageResource(R.drawable.sketch_cabtn_down_180927);
                    back.setVisibility(View.VISIBLE);
                    tvAll.setVisibility(View.VISIBLE);
                    expand(rcvAll);
                    UtilAnim.fideOut(tabBar, 100, null);
                }
                isOpen = !isOpen;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    all.setRotation(90);
                    rcvAll.setVisibility(View.GONE);
                    back.setVisibility(View.GONE);
                } else {
                    all.setRotation(270);
                    rcvAll.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
                isOpen = !isOpen;
            }
        });
        tvOnResumScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final double offsetX = 200.0;
                final double duration = 500.0;
                if (tabBar != null)
                    tabBar.scrollTo((int) offsetX, 0);
                new CountDownTimer((long) duration, 20) {
                    public void onTick(long millisUntilFinished) {
                        int x = (int) (offsetX - (duration - millisUntilFinished) / duration * offsetX);
                        Debug("millisUntilFinished : " + millisUntilFinished + " move x : " + x);
                        if (tabBar != null)
                            tabBar.smoothScrollTo(x, 0);
                    }

                    public void onFinish() {
                        if (tabBar != null)
                            tabBar.smoothScrollTo(0, 0);
                    }
                }.start();
            }

        });

    }

    private ColorStateList createColorStateList(int color_state_pressed, int color_state_selected, int color_state_default) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed}, //pressed
                        new int[]{android.R.attr.state_selected}, // enabled
                        new int[]{} //default
                },
                new int[]{
                        color_state_pressed,
                        color_state_selected,
                        color_state_default
                }
        );
    }

    public void expand(final View v) {
        if (v == null) {
            return;
        }

        if (v.getVisibility() == View.VISIBLE) {
            return;
        }
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300);
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        if (v == null) {
            return;
        }

        if (v.getVisibility() == View.GONE) {
            return;
        }
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300);
        v.startAnimation(a);
    }
}