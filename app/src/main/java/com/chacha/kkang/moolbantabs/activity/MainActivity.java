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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.TAB_DATA;
import com.chacha.kkang.moolbantabs.UtilAnim;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Pager;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Sub;
import com.chacha.kkang.moolbantabs.component.MBTabBar;
import com.chacha.kkang.moolbantabs.component.ViewMainTab;
import com.chacha.kkang.moolbantabs.component.ViewSubTab;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity implements ViewMainTab.setOnMainTabClickListener, ViewSubTab.setOnSubTabClickListener {

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

    ImageView ivAll;
    ImageView ivFading;
    LinearLayout llAll;
    LinearLayout llSub;

    Adapter_Sub adapterSub;
    GridLayoutManager gridLayoutManager2;
    View back;

    ViewPager pager;
    Adapter_Pager adapterPager;

    boolean isOpen = false;
    private static final int mainTabCount = 3;
    private static final int subTabCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                if (j == 0) {
                    sub.isSelect = true;
                }
                data.subList.add(sub);
            }
            tabList.add(data);
        }

        adapterPager = new Adapter_Pager(this, tabList);

        gridLayoutManager2 = new GridLayoutManager(MainActivity.this, 3);

        adapterSub = new Adapter_Sub(this, new Adapter_Sub.setOnSubTabClickListener() {
            @Override
            public void onSubTabClick(int position, TAB_DATA data) {
                if (tabList.size() > pager.getCurrentItem()) {
                    ArrayList<TAB_DATA> subList = tabList.get(pager.getCurrentItem()).subList;
                    for (int i = 0; i < subList.size(); i++) {
                        if (subList.get(i) != null) {
                            subList.get(i).isSelect = false;
                        }
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
        ivAll = (ImageView) findViewById(R.id.ivAll);
        llAll = (LinearLayout) findViewById(R.id.llAll);
        llSub = (LinearLayout) findViewById(R.id.llSub);
        pager = (ViewPager) findViewById(R.id.pager);
        tabBar = (MBTabBar) findViewById(R.id.tabBar);
        rcvSub = (RecyclerView) findViewById(R.id.rcvSub);
        ivFading = (ImageView) findViewById(R.id.ivFading);
    }

    private void setView() {
        pager.setAdapter(adapterPager);
        pager.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        rcvSub.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        tvAll.setVisibility(View.GONE);
        llAll.setVisibility(View.GONE);
        llSub.setVisibility(View.GONE);

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
        updateAllTab();
        updateSubTab(0);
    }

    private void updateAllTab() {
        if (llAll.getChildCount() > 0) {
            llAll.removeAllViews();
        }

        int count = 0;
        if (tabList.size() > 0) {
            if (tabList.size() % mainTabCount != 0) {
                count = mainTabCount - (tabList.size() % mainTabCount);
            }
        }

        LinearLayout ll = null;
        for (int i = 0; i < tabList.size() + count; i++) {
            if (ll == null) {
                ll = new LinearLayout(MainActivity.this);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }

            ViewMainTab tab = new ViewMainTab(MainActivity.this, this);
            tab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            if (i < tabList.size()) {
                tab.setData(tabList.get(i));
                ll.addView(tab);
            } else {
                tab.setData(null);
                ll.addView(tab);
            }

            if (i % mainTabCount == 2) {
                llAll.addView(ll);
            }

            if (ll.getChildCount() == mainTabCount) {
                ll = null;
            }
        }
    }

    private void updateSubTab(int position) {
        if (llSub.getChildCount() > 0) {
            llSub.removeAllViews();
        }

        int count = 0;
        if (tabList.get(position).subList.size() > 0) {
            if (tabList.get(position).subList.size() % subTabCount != 0) {
                count = subTabCount - (tabList.get(position).subList.size() % subTabCount);
            }
        }

        LinearLayout ll = null;
        for (int i = 0; i < tabList.get(position).subList.size() + count; i++) {
            if (ll == null) {
                ll = new LinearLayout(MainActivity.this);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }

            ViewSubTab tab = new ViewSubTab(MainActivity.this, this);
            tab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            if (i < tabList.get(position).subList.size()) {
                tab.setData(tabList.get(position).subList.get(i));
                ll.addView(tab);
            } else {
                tab.setData(null);
                ll.addView(tab);
            }

            if (i % subTabCount == 2) {
                llSub.addView(ll);
            }

            if (ll.getChildCount() == subTabCount) {
                ll = null;
            }
        }
    }

    private void setEvent() {
        tabBar.setOnTabReselectedListener(new MBTabBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Debug("----- onTabSelected   >  " + position + " / " + tabList.get(position).subList.size());
                for (int i = 0; i < tabList.size(); i++) {
                    tabList.get(i).isSelect = false;
                }
                tabList.get(position).isSelect = true;
                updateAllTab();

                if (tabList.get(position).subList.size() > 0) {
                    llSub.setVisibility(View.VISIBLE);
                    updateSubTab(position);
                } else {
                    llSub.setVisibility(View.GONE);
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
        });

        ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    ivAll.setImageResource(R.drawable.sketch_cabtn_up_180927);
                    back.setVisibility(View.GONE);
                    tvAll.setVisibility(View.GONE);
                    ivFading.setVisibility(View.VISIBLE);
                    collapse(llAll);
                    UtilAnim.fideIn(tabBar, 100, null);
                } else {
                    ivAll.setImageResource(R.drawable.sketch_cabtn_down_180927);
                    back.setVisibility(View.VISIBLE);
                    tvAll.setVisibility(View.VISIBLE);
                    ivFading.setVisibility(View.GONE);
                    expand(llAll);
                    UtilAnim.fideOut(tabBar, 100, null);
                }
                isOpen = !isOpen;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    ivAll.setImageResource(R.drawable.sketch_cabtn_up_180927);
                    tvAll.setVisibility(View.GONE);
                    back.setVisibility(View.GONE);
                    ivFading.setVisibility(View.VISIBLE);
                    collapse(llAll);
                    UtilAnim.fideIn(tabBar, 100, null);
                } else {
                    ivAll.setImageResource(R.drawable.sketch_cabtn_down_180927);
                    tvAll.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    ivFading.setVisibility(View.GONE);
                    expand(llAll);
                    UtilAnim.fideOut(tabBar, 100, null);
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

    @Override
    public void onMainTabClick(TAB_DATA data) {
        for (int i = 0; i < tabList.size(); i++) {
            tabList.get(i).isSelect = false;
        }
        data.isSelect = true;
        updateAllTab();
    }

    @Override
    public void onSubTabClick(TAB_DATA data) {
        if (tabList.size() > pager.getCurrentItem()) {
            ArrayList<TAB_DATA> subList = tabList.get(pager.getCurrentItem()).subList;
            for (int i = 0; i < subList.size(); i++) {
                if (subList.get(i) != null) {
                    subList.get(i).isSelect = false;
                }
            }

            data.isSelect = true;
            updateSubTab(pager.getCurrentItem());
        }
    }
}
