package com.kkang.mbtabs.component;

import android.content.Context;
import android.content.res.ColorStateList;
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

public class MBTab extends LinearLayout implements ViewTab.setOnTabClickListener {
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
    private ImageView ivAll;
    private TextView tvAll;
    private LinearLayout llAllBtn;
    private LinearLayout llSub;
    private ArrayList<CUSTOM_TAB_DATA> tabList;
    private LinearLayout llAll;

    boolean isOpen = false;
    private int mainTabCount = 3;
    private int subTabCount = 3;

    private int mainTabSelectRes = R.drawable.mb_shape_round_tomato_trans_7;
    private int mainTabNoSelectRes = R.drawable.mb_shape_round_line01_white_7;
    private int mainTabMargin = intToDp(getContext(), 3);
    private String mainTabSelectColor = "#e84418";
    private String mainTabNoSelectColor = "#231916";
    private int mainTabPadding = 0;
    private boolean isMainTabNoImgShow = false;
    private int mainTabNoImgRes = R.drawable.sketch_fish_180927;
    private int mainTabNoImgPadding = intToDp(getContext(), 10);

    private int subTabRes = R.drawable.mb_shape_round_line01_white_7;
    private String subTabSelectColor = "#3e3e3e";
    private String subTabNoSelectColor = "#878787";
    private int subTabMargin = floatToDp(getContext(), 1.5f);
    private int subTabPadding = intToDp(getContext(), 10);
    private boolean isSubTabNoImgShow = true;
    private int subTabNoImgRes = R.drawable.sketch_fish_180927;
    private int subTabNoImgPadding = intToDp(getContext(), 10);

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

    public void setData(ArrayList<CUSTOM_TAB_DATA> tabList, LinearLayout llAll) {
        this.tabList = tabList;
        this.llAll = llAll;
        if (llAll == null) {
            llAllBtn.setVisibility(View.GONE);
            ivFading.setVisibility(View.GONE);
            tvAll.setVisibility(View.GONE);
        }
        updateView();
        setEvent();
    }

    private void setUI() {
        tabBar = (MBTabBar) findViewById(R.id.tabBar);
        llTab = (LinearLayout) findViewById(R.id.llTab);
        ivFading = (ImageView) findViewById(R.id.ivFading);
        ivAll = (ImageView) findViewById(R.id.ivAll);
        tvAll = (TextView) findViewById(R.id.tvAll);
        llAllBtn = (LinearLayout) findViewById(R.id.llAllBtn);
        llSub = (LinearLayout) findViewById(R.id.llSub);
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
                    tvAll.setVisibility(View.GONE);
                    ivFading.setVisibility(View.VISIBLE);
                    collapse(llAll);
                    UtilAnim.fideIn(tabBar, 100, null);
                } else {
                    ivAll.setImageResource(R.drawable.sketch_cabtn_down_180927);
                    tvAll.setVisibility(View.VISIBLE);
                    ivFading.setVisibility(View.GONE);
                    expand(llAll);
                    UtilAnim.fideOut(tabBar, 100, null);
                }
                isOpen = !isOpen;
            }
        });
    }

    private void updateView() {
        tvAll.setVisibility(View.GONE);
        llSub.setVisibility(View.GONE);

        UtilAnim.fideIn(tabBar, 200, null);

        tabBar.setDividerWidth(0);
        tabBar.setIndicatorHeight(5);
        tabBar.setBackgroundColor(Color.WHITE);

        tabBar.setIndicatorColor(getResources().getColor(R.color.tomato));
        ColorStateList colorList = createColorStateList(getResources().getColor(R.color.tomato)
                , getResources().getColor(R.color.tomato), Color.BLACK);

        tabBar.setTextColor(colorList);
        updateAllTab();
        updateSubTab(0);
    }

    private void updateAllTab() {
        if (llAll == null) {
            return;
        }

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
                ll = new LinearLayout(getContext());
                ll.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }

            ViewTab tab = new ViewTab(getContext(), this);
            tab.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            tab.setTabType("Main");
            tab.setTabResource(mainTabSelectRes, mainTabNoSelectRes);
            tab.setTabMargin(mainTabMargin, mainTabMargin, mainTabMargin, mainTabMargin);
            tab.setTabSetting(mainTabSelectColor, mainTabNoSelectColor);
            tab.setTabPadding(mainTabPadding, mainTabPadding, mainTabPadding, mainTabPadding);
            tab.setNonImgVisible(isMainTabNoImgShow);
            tab.setNonImgResource(mainTabNoImgRes);
            tab.setNonImgPadding(mainTabNoImgPadding, mainTabNoImgPadding, mainTabNoImgPadding, mainTabNoImgPadding);

            if (i < tabList.size()) {
                tab.setData(tabList.get(i));
                ll.addView(tab);
            } else {
                tab.setData(null);
                ll.addView(tab);
            }

            if (i % mainTabCount == (mainTabCount - 1)) {
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
                ll = new LinearLayout(getContext());
                ll.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }

            ViewTab tab = new ViewTab(getContext(), this);
            tab.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            tab.setTabType("Sub");
            tab.setTabResource(subTabRes, subTabRes);
            tab.setTabSetting(subTabSelectColor, subTabNoSelectColor);
            tab.setTabMargin(subTabMargin, subTabMargin, subTabMargin, subTabMargin);
            tab.setTabPadding(subTabPadding, subTabPadding, subTabPadding, subTabPadding);
            tab.setNonImgVisible(isSubTabNoImgShow);
            tab.setNonImgResource(subTabNoImgRes);
            tab.setNonImgPadding(subTabNoImgPadding, subTabNoImgPadding, subTabNoImgPadding, subTabNoImgPadding);

            if (i < tabList.get(position).subList.size()) {
                tab.setData(tabList.get(position).subList.get(i));
                ll.addView(tab);
            } else {
                tab.setData(null);
                ll.addView(tab);
            }

            if (i % subTabCount == (subTabCount - 1)) {
                llSub.addView(ll);
            }

            if (ll.getChildCount() == subTabCount) {
                ll = null;
            }
        }
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
    public void onTabClick(String type, CUSTOM_TAB_DATA data) {
        if(type.equals("Main")){
            for (int i = 0; i < tabList.size(); i++) {
                if (tabList.get(i) != null) {
                    tabList.get(i).isSelect = false;
                }
            }
            data.isSelect = true;
            updateAllTab();
        }else if(type.equals("Sub")){
            if (viewPager != null) {
                int position = viewPager.getCurrentItem();
                ArrayList<CUSTOM_TAB_DATA> subList = tabList.get(position).subList;
                for (int i = 0; i < subList.size(); i++) {
                    if (subList.get(i) != null) {
                        subList.get(i).isSelect = false;
                    }
                }
                data.isSelect = true;
                updateSubTab(position);
            }
        }
    }

    public void setTabSize(int width, int height) {
        int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        llTab.setLayoutParams(params);
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

    public void setVisibleAll(boolean isShowllAllBtn) {
        if (llAll == null) {
            return;
        }

        if (isShowllAllBtn) {
            llAllBtn.setVisibility(View.VISIBLE);
        } else {
            llAllBtn.setVisibility(View.GONE);
        }
    }

    public void setFadingResource(int resource) {
        if (llAll == null) {
            return;
        }

        ivFading.setImageResource(resource);
    }

    public void setVisibleFading(boolean isShowFading) {
        if (isShowFading) {
            ivFading.setVisibility(View.VISIBLE);
        } else {
            ivFading.setVisibility(View.GONE);
        }
    }

    public void setSubTabBackground(String colorCode) {
        llSub.setBackgroundColor(Color.parseColor(colorCode));
    }

    public void setSubTabPadding(int left, int top, int right, int bottom) {
        llSub.setPadding(left, top, right, bottom);
    }

    public void setMainTabCount(int mainCount) {
        mainTabCount = mainCount;
        updateView();
    }

    public void setSubTabCount(int subCount) {
        subTabCount = subCount;
        updateView();
    }

    public void setMainTabResource(int selectRes, int noSelectRes) {
        mainTabSelectRes = selectRes;
        mainTabNoSelectRes = noSelectRes;
    }

    public void setMainTabMargin(int dp) {
        mainTabMargin = dp;
    }

    public void setMainTabPadding(int dp) {
        mainTabPadding = dp;
    }

    public void setMainTabSetting(String mainTabSelectColor, String mainTabNoSelectColor) {
        this.mainTabSelectColor = mainTabSelectColor;
        this.mainTabNoSelectColor = mainTabNoSelectColor;
    }

    public void setMainTabNoImgVisible(boolean isShow) {
        isMainTabNoImgShow = isShow;
    }

    public void setMainTabNoImgResource(int resource) {
        mainTabNoImgRes = resource;
    }

    public void setMainTabNoImgPadding(int dp) {
        mainTabNoImgPadding = dp;
    }

    public void setSubTabResource(int resource) {
        subTabRes = resource;
    }

    public void setSubTabSetting(String selectColor, String noSelectColor) {
        subTabSelectColor = selectColor;
        subTabNoSelectColor = noSelectColor;
    }

    public void setSubTabMargin(int dp) {
        subTabMargin = dp;
    }

    public void setSubTabPadding(int dp) {
        subTabPadding = dp;
    }

    public void setSubTabNoImgVisible(boolean isShow) {
        isSubTabNoImgShow = isShow;
    }

    public void setSubTabNoImgResource(int resource) {
        subTabNoImgRes = resource;
    }

    public void setSubTabNoImgPadding(int dp) {
        subTabNoImgPadding = dp;
    }

    public static int intToDp(Context context, int value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public static int floatToDp(Context context, float value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }
}