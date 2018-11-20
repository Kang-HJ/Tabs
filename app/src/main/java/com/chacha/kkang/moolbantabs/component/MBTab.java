package com.chacha.kkang.moolbantabs.component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import com.chacha.kkang.moolbantabs.activity.MainActivity;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.chacha.kkang.moolbantabs.activity.MainActivity.Debug;

public class MBTab extends LinearLayout implements ViewMainTab.setOnMainTabClickListener, ViewSubTab.setOnSubTabClickListener {

    public MBTabBar tabBar;
    private LinearLayout llTab;
    private ImageView ivFading;
    private ImageView ivAll;
    private TextView tvAll;
    private LinearLayout llAllBtn;
    private LinearLayout llSub;
    private ArrayList<TAB_DATA> tabList;
    private LinearLayout llAll;

    boolean isOpen = false;
    private int mainTabCount = 3;
    private int subTabCount = 3;

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

    public void setData(ArrayList<TAB_DATA> tabList, LinearLayout llAll){
        this.tabList = tabList;
        this.llAll = llAll;
        updateView();
        setEvent();
    }

    public void setTabSize(int width, int height) {
        int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        llTab.setLayoutParams(params);
    }

    public void setCollectTabSetting(String title, String colorCode, int size) {
        tvAll.setText(title);
        tvAll.setTextColor(Color.parseColor(colorCode));
        tvAll.setTextSize(size);
    }

    public void setVisibleAll(boolean isShowllAllBtn) {
        if (isShowllAllBtn) {
            llAllBtn.setVisibility(View.VISIBLE);
        } else {
            llAllBtn.setVisibility(View.GONE);
        }
    }

    public void setFadingResource(int resource) {
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

            ViewMainTab tab = new ViewMainTab(getContext(), this);
            tab.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
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
                ll = new LinearLayout(getContext());
                ll.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }

            ViewSubTab tab = new ViewSubTab(getContext(), this);
            tab.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
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
    public void onSubTabClick(TAB_DATA data) {
        if (tabList.size() > ((MainActivity) getContext()).pager.getCurrentItem()) {
            ArrayList<TAB_DATA> subList = tabList.get(((MainActivity) getContext()).pager.getCurrentItem()).subList;
            for (int i = 0; i < subList.size(); i++) {
                if (subList.get(i) != null) {
                    subList.get(i).isSelect = false;
                }
            }

            data.isSelect = true;
            updateSubTab(((MainActivity) getContext()).pager.getCurrentItem());
        }
    }

    @Override
    public void onMainTabClick(TAB_DATA data) {
        for (int i = 0; i < tabList.size(); i++) {
            tabList.get(i).isSelect = false;
        }
        data.isSelect = true;
        updateAllTab();
        ((MainActivity) getContext()).pager.setCurrentItem(Integer.valueOf(data.key));
        ivAll.performClick();
    }
}