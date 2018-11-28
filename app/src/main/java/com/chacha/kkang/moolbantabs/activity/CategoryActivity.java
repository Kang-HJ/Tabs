package com.chacha.kkang.moolbantabs.activity;

import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Pager;
import com.kkang.mbtabs.component.CUSTOM_TAB_DATA;
import com.kkang.mbtabs.component.MBTab;
import com.kkang.mbtabs.component.ViewMainTab;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class CategoryActivity extends AppCompatActivity {

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

    private MBTab mbTab;
    private ArrayList<CUSTOM_TAB_DATA> tabList;

    public ViewPager pager;
    private Adapter_Pager adapterPager;
    private TextView tvOnResumScroll;

    private ViewMainTab viewMainTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initData();
        setUI();
        setEvent();
        setView();
    }

    private void initData() {
        tabList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            CUSTOM_TAB_DATA data = new CUSTOM_TAB_DATA();
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
                CUSTOM_TAB_DATA sub = new CUSTOM_TAB_DATA();
                sub.key = i + "";
                sub.name = i + " SUB TAB";
                data.isSelect = false;
                if (j == 0) {
                    sub.isSelect = true;
                }
                data.subList.add(sub);
            }
            tabList.add(data);
        }

        adapterPager = new Adapter_Pager(CategoryActivity.this, tabList);
    }

    private void setUI() {
        pager = (ViewPager) findViewById(R.id.pager);
        mbTab = (MBTab) findViewById(R.id.mbTab);
        tvOnResumScroll = (TextView) findViewById(R.id.tvOnResumScroll);
        viewMainTab = (ViewMainTab) findViewById(R.id.viewMainTab);
    }

    private void setEvent() {
        tvOnResumScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final double offsetX = 200.0;
                final double duration = 500.0;
                if (mbTab.tabBar != null)
                    mbTab.tabBar.scrollTo((int) offsetX, 0);
                new CountDownTimer((long) duration, 20) {
                    public void onTick(long millisUntilFinished) {
                        int x = (int) (offsetX - (duration - millisUntilFinished) / duration * offsetX);
                        Debug("millisUntilFinished : " + millisUntilFinished + " move x : " + x);
                        if (mbTab.tabBar != null)
                            mbTab.tabBar.smoothScrollTo(x, 0);
                    }

                    public void onFinish() {
                        if (mbTab.tabBar != null)
                            mbTab.tabBar.smoothScrollTo(0, 0);
                    }
                }.start();
            }

        });


    }

    private void setView() {
        pager.setAdapter(adapterPager);
        pager.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);

        mbTab.setData(tabList, true);
        mbTab.setViewPager(pager);

        mbTab.setListener(new MBTab.setTabClickListener() {
            @Override
            public void onTabClickListener(int position) {
                pager.setCurrentItem(position);
                viewMainTab.setSelectedTab(position);
            }

            @Override
            public void onAllClickListener(boolean isOpen) {
                if(isOpen){
                    expand(viewMainTab);
                }else{
                    collapse(viewMainTab, new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        });
        viewMainTab.setVisibility(View.GONE);
        viewMainTab.setData(tabList, new ViewMainTab.setMainTabClickListener() {
            @Override
            public void onMainTabclickListener(int position, CUSTOM_TAB_DATA data) {
                pager.setCurrentItem(position);
                collapse(viewMainTab, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mbTab.setAllBtnState(false);
            }
        });
    }
    public void expand(final View v) {
        if (v == null) {
            return;
        }

        if (v.getVisibility() == View.VISIBLE) {
            return;
        }
        v.measure(MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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


    public void collapse(final View v, Animation.AnimationListener animationListener) {
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

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));

        a.setAnimationListener(animationListener);
        v.startAnimation(a);
    }
}
