package com.kkang.mbtabs.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.kkang.mbtabs.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ViewSubTab extends LinearLayout {

    LinearLayout llSubContainer;
    LinearLayout llSub;

    ArrayList<CUSTOM_TAB_DATA> subList;
    private ViewTab[] subTab;

    private int subTabRes = R.drawable.mb_shape_round_line01_white_7;
    private String subTabSelectColor = "#3e3e3e";
    private String subTabNoSelectColor = "#878787";
    private int subTabMargin = 0;
    private boolean isSubTabNoImgShow = true;
    private int subTabNoImgRes = R.drawable.sketch_fish_180927;

    private int minTabCount = 0;
    private int subTabCount = 3;

    setSubTabClickListener listener;

    public ViewSubTab(Context context) {
        super(context);
        init();
    }

    public ViewSubTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewSubTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.view_mb_sub_tab, this);

        subTabMargin = floatToDp(getContext(), 1.5f);

        setUI();
    }

    private void setUI() {
        llSub = (LinearLayout) findViewById(R.id.llSub);
        llSubContainer = (LinearLayout) findViewById(R.id.llSubContainer);
    }

    public void setData(ArrayList<CUSTOM_TAB_DATA> subList, setSubTabClickListener listener) {
        this.subList = subList;
        this.listener = listener;
        updateUI();
    }

    public void setSelectedTab(int position) {

        if (subList.size() > position) {
            for (int i = 0; i < subList.size(); i++) {
                subList.get(i).isSelect = false;
                if (i == position) {
                    subList.get(i).isSelect = true;
                }
                if (subTab.length > i) {
                    subTab[i].updateUI();
                }
            }
        }
    }

    public void setMinTabCount(int minTabCount) {
        this.minTabCount = minTabCount;
        updateUI();
    }

    public void updateUI() {
        if (subList == null) {
            return;
        }

        subTabCount = 3;
        int listSize = subList.size();
        if (listSize > 3) {
            if (listSize % 3 == 1) {
                subTabCount = 4;
            }
        }

        int count = 0;

        if (listSize > 0) {
            if (subList.size() % subTabCount != 0) {
                count = subTabCount - (subList.size() % subTabCount);
            }
            llSub.removeAllViews();
        }

        if (subList.size() > minTabCount) {  //전체, 탭 이름
            llSubContainer.setVisibility(View.VISIBLE);
        } else {
            llSubContainer.setVisibility(View.GONE);
        }

        if (subTab != null) {
            subTab = null;
        }

        int countTab = subList.size() + count;
        subTab = new ViewTab[countTab];


        LinearLayout ll = null;
        for (int i = 0; i < subList.size() + count; i++) {
            if (ll == null) {
                ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }

            subTab[i] = new ViewTab(getContext(), new ViewTab.setOnTabClickListener() {
                @Override
                public void onTabClick(CUSTOM_TAB_DATA data, int position) {
                    onSubTabClick(data, position);

                }
            });
            subTab[i].setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT, 1.0f));
            subTab[i].setTabType("Sub");
            subTab[i].setTabResource(subTabRes, subTabRes);
            subTab[i].setTabSetting(subTabSelectColor, subTabNoSelectColor);
            subTab[i].setTabMargin(subTabMargin, subTabMargin, subTabMargin, subTabMargin);
            subTab[i].setNonImgVisible(isSubTabNoImgShow);
            subTab[i].setNonImgResource(subTabNoImgRes);

            if (i < subList.size()) {
                subTab[i].setData(subList.get(i), i);
                ll.addView(subTab[i]);
            } else {
                subTab[i].setData(null, 0);
                ll.addView(subTab[i]);
            }

            if (i % subTabCount == (subTabCount - 1)) {
                llSub.addView(ll);
                LayoutParams params = (LayoutParams) ll.getLayoutParams();
                params.width = MATCH_PARENT;
                params.height = intToDp(getContext(), 37);
                ll.setLayoutParams(params);
            }

            if (ll.getChildCount() == subTabCount) {
                ll = null;
            }
        }
    }

    private void onSubTabClick(CUSTOM_TAB_DATA data, int position) {
        for (int i = 0; i < subList.size(); i++) {
            if (subList.get(i) != null) {
                subList.get(i).isSelect = false;
                if (position == i) {
                    subList.get(i).isSelect = true;
                }
                subTab[i].updateUI();
            }
        }
        if (listener != null) {
            listener.onSubTabclickListener(position, data);
        }
    }

    public static int intToDp(Context context, int value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public static int floatToDp(Context context, float value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public interface setSubTabClickListener {
        void onSubTabclickListener(int position, CUSTOM_TAB_DATA data);
    }
}
