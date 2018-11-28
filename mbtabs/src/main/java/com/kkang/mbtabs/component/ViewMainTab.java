package com.kkang.mbtabs.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.kkang.mbtabs.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ViewMainTab extends LinearLayout {

    private LinearLayout llAll;
    private LinearLayout llAllContainer;

    ArrayList<CUSTOM_TAB_DATA> tabList;

    private ViewTab[] mainTab;

    setMainTabClickListener listener;

    private int mainTabCount = 3;


    private int mainTabSelectRes = R.drawable.mb_shape_round_tomato_trans_7;
    private int mainTabNoSelectRes = R.drawable.mb_shape_round_line01_white_7;
    private int mainTabMargin = intToDp(getContext(), 3);
    private String mainTabSelectColor = "#e84418";
    private String mainTabNoSelectColor = "#231916";
    private boolean isMainTabNoImgShow = false;
    private int mainTabNoImgRes = R.drawable.sketch_fish_180927;


    public ViewMainTab(Context context) {
        super(context);
        init();
    }

    public ViewMainTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewMainTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.view_mb_main_tab, this);

        setUI();
    }

    private void setUI() {
        llAll = (LinearLayout) findViewById(R.id.llAll);
        llAllContainer = (LinearLayout) findViewById(R.id.llAllContainer);
    }

    public void setData(ArrayList<CUSTOM_TAB_DATA> tabList, setMainTabClickListener listener) {
        this.tabList = tabList;
        this.listener = listener;
        updateUI();
    }

    public void setSelectedTab(int position) {

        if (tabList.size() > position) {
            for (int i = 0; i < tabList.size(); i++) {
                tabList.get(i).isSelect = false;
                if (i == position) {
                    tabList.get(i).isSelect = true;
                }
                if (mainTab.length > i) {
                    mainTab[i].updateUI();
                }
            }
        }
    }

    public void updateUI() {
        if (llAllContainer == null || llAll == null) {
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

            mainTab = new ViewTab[tabList.size() + count];
        }

        LinearLayout ll = null;
        for (int i = 0; i < tabList.size() + count; i++) {
            if (ll == null) {
                ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }

            mainTab[i] = new ViewTab(getContext(), new ViewTab.setOnTabClickListener() {
                @Override
                public void onTabClick(CUSTOM_TAB_DATA data, int position) {
                    onMainTabClick(data, position);
                }
            });
            mainTab[i].setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT, 1.0f));
            mainTab[i].setTabType("Main");
            mainTab[i].setTabResource(mainTabSelectRes, mainTabNoSelectRes);
            mainTab[i].setTabMargin(mainTabMargin, mainTabMargin, mainTabMargin, mainTabMargin);
            mainTab[i].setTabSetting(mainTabSelectColor, mainTabNoSelectColor);
            mainTab[i].setNonImgVisible(isMainTabNoImgShow);
            mainTab[i].setNonImgResource(mainTabNoImgRes);

            if (i < tabList.size()) {
                mainTab[i].setData(tabList.get(i), i);
                ll.addView(mainTab[i]);
            } else {
                mainTab[i].setData(null, 0);
                ll.addView(mainTab[i]);
            }

            if (i % mainTabCount == (mainTabCount - 1)) {
                llAll.addView(ll);
                LayoutParams params = (LayoutParams) ll.getLayoutParams();
                params.width = MATCH_PARENT;
                params.height = intToDp(getContext(), 37);
                ll.setLayoutParams(params);
            }

            if (ll.getChildCount() == mainTabCount) {
                ll = null;
            }
        }

        MBTab.Debug("llAll "+llAll.getChildCount());
        MBTab.Debug("tabList "+tabList.size());
    }

    private void onMainTabClick(CUSTOM_TAB_DATA data, int position) {
        for (int i = 0; i < tabList.size(); i++) {
            if (tabList.get(i) != null) {
                tabList.get(i).isSelect = false;
                if (position == i) {
                    tabList.get(i).isSelect = true;
                }
                mainTab[i].updateUI();
            }
        }
        if (listener != null) {
            listener.onMainTabclickListener(position, data);
        }
    }

    public static int intToDp(Context context, int value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public static int floatToDp(Context context, float value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public interface setMainTabClickListener {
        void onMainTabclickListener(int position, CUSTOM_TAB_DATA data);
    }
}
