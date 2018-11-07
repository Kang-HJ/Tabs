package com.chacha.kkang.moolbantabs;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kkang on 2018. 10. 28..
 */

public class TabBar extends HorizontalScrollView {
    OnTabClicked mOnTabClicked;
    LinearLayout layoutContainer;
    ArrayList<TAB_DATA> data = new ArrayList<>();
    ArrayList<View> views = new ArrayList<>();
    boolean isSlide = false;

    boolean isSub = false;
    int width = 0;
    int wrapWidth = 0;
    ViewPager viewPager;

    // Log
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


    public TabBar(Context context) {
        super(context);
        initView(context, null);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        setFillViewport(true);
        setHorizontalScrollBarEnabled(false);
        layoutContainer = new LinearLayout(context);

        layoutContainer.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        layoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(layoutContainer);
        Debug(" init   " + layoutContainer.getMeasuredWidth());
        Display dis = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        width = dis.getWidth();
    }


    public void setOnTabClicked(OnTabClicked onTabClicked) {
        mOnTabClicked = onTabClicked;
    }

    public void notifyDataSetChanged() {
        layoutContainer.removeAllViews();
        views.clear();
        Debug("notifyDataSetChanged    " + width + " width");
        wrapWidth = 0;
        for (int i = 0; i < data.size(); i++) {
            View view = createTabView(data.get(i), i);

            layoutContainer.addView(view, new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            views.add(view);

            final int finalI = i;
            layoutContainer.getChildAt(i).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    /* don't forget to remove the listener after you use it once */
                    if (layoutContainer.getChildAt(finalI) == null) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        layoutContainer.getChildAt(finalI).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    Debug(" notifyDataSetChanged    layoutContainer   width:" + layoutContainer.getChildAt(finalI).getWidth());
                    wrapWidth += layoutContainer.getChildAt(finalI).getWidth();
                    tabWidthSetting();
                    Debug("notifyDataSetChanged    wrapWidth : " + wrapWidth);
                }
            });
        }
    }

    private void tabWidthSetting() {
        boolean isWeight = false;
        if (wrapWidth <= width) {
            isWeight = true;
        }

        if (isWeight) {
            for (int i = 0; i < data.size(); i++) {
                layoutContainer.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, 1));
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                layoutContainer.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
    }

    public void addTab(ArrayList<TAB_DATA> data, boolean isSub) {
        this.data = data;
        this.isSub = isSub;

    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Debug(" position getWidth : : " + layoutContainer.getChildAt(position).getWidth());
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public void clearTabs() {
        layoutContainer.removeAllViews();
    }

    public void setSelectTab(int position) {

        if (layoutContainer == null) {
            return;
        }

        if (layoutContainer.getChildCount() <= position) {
            return;
        }

        View view = layoutContainer.getChildAt(position);
        if (view == null) {
            return;
        }

        LinearLayout linearLayout = view.findViewById(R.id.llMain);
        if (linearLayout == null) {
            return;
        }

        linearLayout.performClick();
    }

    private View createTabView(final TAB_DATA tab, final int position) {

        if (isSub) {
            ItemTalkSubCategory view = new ItemTalkSubCategory(getContext());
            view.setData(tab);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUI(position);
                }
            });
            return view;
        } else {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab, layoutContainer, false);
            LinearLayout llMain = (LinearLayout) view.findViewById(R.id.llMain);
            llMain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateUI(position);
                }
            });
            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView line = (TextView) view.findViewById(R.id.line);
            ImageView ivNew = (ImageView) view.findViewById(R.id.ivNew);
            ivNew.setVisibility(View.GONE);

            if (tab.isNew) {
                ivNew.setVisibility(View.VISIBLE);
            }
            llMain.setBackgroundResource(0);
            line.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.line_01));
            tvName.setTextColor(Color.BLACK);

            if (tab.isSelect) {
                //llMain.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_bottom_line_tab));
                line.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tomato));
                tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.tomato));
            }
            tvName.setText(tab.name);
            return view;
        }

    }

    public void updateUI(int position) {
        if (isSlide) {
            return;
        }
        isSlide = true;
        if (isSub) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).isSelect = false;
                if (position == i) {
                    data.get(position).isSelect = true;
                }
                ((ItemTalkSubCategory) views.get(i)).setData(data.get(i));
            }

        } else {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).isSelect = false;
                LinearLayout llMain = (LinearLayout) views.get(i).findViewById(R.id.llMain);
                TextView tvName = (TextView) views.get(i).findViewById(R.id.tvName);
                TextView line = (TextView) views.get(i).findViewById(R.id.line);
                line.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.line_01));
                ImageView ivNew = (ImageView) views.get(i).findViewById(R.id.ivNew);
                ivNew.setVisibility(View.GONE);
                if (data.get(i).isNew) {
                    ivNew.setVisibility(View.VISIBLE);
                }
                tvName.setTextColor(Color.BLACK);
                if (position == i) {
                    data.get(position).isSelect = true;
                    //llMain.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_bottom_line_tab));
                    line.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tomato));
                    tvName.setTextColor(ContextCompat.getColor(getContext(), R.color.tomato));

                }
            }
        }
        ScrollTo(position);
    }

    private void ScrollTo(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Debug( " ONCLICK -->  " + getScrollX());
                Debug(" ONCLICK  R  " + layoutContainer.getChildAt(position).getRight());
                Debug(" ONCLICK  L  " + layoutContainer.getChildAt(position).getLeft());

                int endPos = (int) layoutContainer.getChildAt(position).getLeft();
                int halfWidth = (int) (Math.abs(layoutContainer.getChildAt(position).getLeft() - layoutContainer.getChildAt(position).getRight()) / 2);
                Debug(" ONCLICK  halfWidth  " + halfWidth);
                int move = endPos + halfWidth - (width / 2);
                smoothScrollTo(move, 0);
                Debug(" move    " + move);
                mOnTabClicked.onTabClicked(isSub, data.get(position), position);
                isSlide = false;
            }
        }, 50);
    }

    public interface OnTabClicked {
        public void onTabClicked(boolean isSub, TAB_DATA tab, int pos);
    }
}





