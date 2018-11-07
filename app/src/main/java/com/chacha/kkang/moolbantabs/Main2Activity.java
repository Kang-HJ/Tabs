package com.chacha.kkang.moolbantabs;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import static com.chacha.kkang.moolbantabs.TabBar.Debug;

public class Main2Activity extends AppCompatActivity implements Adapter_All.OnSubAreaItemClickListener {
    PagerSlidingTabStrip tabBar;
    RecyclerView rcvSub;
    ArrayList<TAB_DATA> tabList;
    ArrayList<TAB_DATA> subList;
    TextView scroll;
    TextView tvAll;

    ImageView all;
    RecyclerView rcvAll;

    Adapter_Pager adapterPager;
    Adapter_All adapterAll;
    Adapter_Sub adapterSub;
    GridLayoutManager gridLayoutManager1;
    GridLayoutManager gridLayoutManager2;
    View back;

    ViewPager pager;
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
        subList = new ArrayList<>();

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
            tabList.add(data);
        }

        adapterPager = new Adapter_Pager(this, tabList);
        adapterAll = new Adapter_All(this, tabList, this);
        gridLayoutManager1 = new GridLayoutManager(Main2Activity.this, 3);

        adapterSub = new Adapter_Sub(this, subList, new Adapter_Sub.OnSubAreaItemClickListener() {
            @Override
            public void onSubItemClick(int position, TAB_DATA data, View v) {
                for (int i = 0; i < subList.size(); i++) {
                    subList.get(i).isSelect = false;
                }
                data.isSelect = true;
                adapterSub.notifyDataSetChanged();
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(v);
            }

            @Override
            public void onSubItemClick(int position, TAB_DATA data) {

            }
        });
        gridLayoutManager2 = new GridLayoutManager(Main2Activity.this, 3);

    }

    private void setUI() {
        back = (View) findViewById(R.id.back);
        scroll = (TextView) findViewById(R.id.scroll);
        tvAll = (TextView) findViewById(R.id.tvAll);
        all = (ImageView) findViewById(R.id.all);
        rcvAll = (RecyclerView) findViewById(R.id.rcvAll);
        pager = (ViewPager) findViewById(R.id.pager);

        tabBar = (PagerSlidingTabStrip) findViewById(R.id.tabBar);
        rcvSub = (RecyclerView) findViewById(R.id.rcvSub);


    }

    private void setView(){
        pager.setAdapter(adapterPager);
        pager.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        rcvAll.setLayoutManager(gridLayoutManager1);
        rcvAll.setAdapter(adapterAll);

        all.setRotation(90);
        rcvAll.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        tvAll.setVisibility(View.GONE);

        UtilAnim.fideIn(tabBar, 200, null);

        rcvSub.setLayoutManager(gridLayoutManager2);
        rcvSub.setAdapter(adapterSub);

        updateSubCate();
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
                    all.setRotation(90);
                    //rcvAll.setVisibility(View.GONE);
                    back.setVisibility(View.GONE);
                    tvAll.setVisibility(View.GONE);
                    collapse(rcvAll);
                    UtilAnim.fideIn(tabBar, 200, null);
                } else {
                    all.setRotation(270);
                    //rcvAll.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    tvAll.setVisibility(View.VISIBLE);
                    expand(rcvAll);
                    UtilAnim.fideOut(tabBar, 200, null);
                    UtilAnim.moveXBounce(tvAll, UtilAnim.dip2px(Main2Activity.this, 90), 0, 500, null);
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
        scroll.setOnClickListener(new View.OnClickListener() {
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
        tabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                           @Override
                                           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                           }

                                           @Override
                                           public void onPageSelected(int position) {

                                           }

                                           @Override
                                           public void onPageScrollStateChanged(int state) {

                                           }

                                       }

        );

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

    private void updateSubCate() {

        subList.clear();
        for (int i = 0; i < 7; i++) {
            TAB_DATA data = new TAB_DATA();
            data.key = i + "";
            data.name = i + " SUB TAB";
            data.isSelect = false;

            subList.add(data);

        }
        adapterSub.setData(subList);
        adapterSub.notifyDataSetChanged();

    }

    @Override
    public void onSubItemClick(int position, TAB_DATA data) {
        for (int i = 0; i < tabList.size(); i++) {
            tabList.get(i).isSelect = false;
        }
        data.isSelect = true;
        adapterAll.notifyDataSetChanged();
    }

    @Override
    public void onSubItemClick(int position, TAB_DATA data, View v) {

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
