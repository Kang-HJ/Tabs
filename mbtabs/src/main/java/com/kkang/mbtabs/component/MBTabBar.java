package com.kkang.mbtabs.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkang.mbtabs.R;

import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kkang on 2018. 10. 29..
 */

public class MBTabBar extends HorizontalScrollView {
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

    private LinearLayout tabsContainer;
    private LinearLayout.LayoutParams tabLayoutParams;

    private final PageListener pageListener = new PageListener();
    private setOnTabSelectedListener tabSelectedListener = null;
    public ViewPager viewPager;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;

    private int indicatorColor;
    private int indicatorHeight = 2;

    private int scrollOffset = 0;
    private int lastScrollX = 0;
    private ArrayList<CUSTOM_TAB_DATA> tabList;

    Typeface tabTextTypeface;
    int   tabTextTypefaceStyle = 0;

    public MBTabBar(Context context) {
        this(context, null);
    }

    public MBTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MBTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        tabList = new ArrayList<>();
        indicatorColor = getResources().getColor(R.color.tomato);

        String tabTextTypefaceName = "notosanscjk";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabTextTypefaceName = "notosanscjk";
            tabTextTypefaceStyle = Typeface.BOLD;
        }

        tabTextTypeface = Typeface.create(tabTextTypefaceName, tabTextTypefaceStyle);

        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(tabsContainer);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        setTabsContainerParentViewPaddings();
        tabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        Display dis = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        width = dis.getWidth();
    }

    private void setTabsContainerParentViewPaddings() {
        int bottomMargin = indicatorHeight >= 0 ? indicatorHeight : 0;
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), bottomMargin);
    }

    public void setViewPager(ViewPager pager) {
        this.viewPager = pager;
        viewPager.addOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    int wrapWidth = 0;
    int width = 0;

    public void notifyDataSetChanged() {
        wrapWidth = 0;
        tabsContainer.removeAllViews();
        View tabView;
        for (int i = 0; i < tabList.size(); i++) {
            tabView = LayoutInflater.from(getContext()).inflate(R.layout.item_mb_tab, this, false);

            addTab(i, tabView, tabList.get(i));
            final int finalI = i;
            tabsContainer.getChildAt(i).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (tabsContainer.getChildAt(finalI) == null) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tabsContainer.getChildAt(finalI).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    Debug(" notifyDataSetChanged    layoutContainer   width:" + tabsContainer.getChildAt(finalI).getWidth());
                    wrapWidth += tabsContainer.getChildAt(finalI).getWidth();
                    tabWidthSetting();
                    Debug("notifyDataSetChanged    wrapWidth : " + wrapWidth);
                }
            });
        }

        updateTabStyles();
        tabWidthSetting();
    }

    public void setData(ArrayList<CUSTOM_TAB_DATA> tabList) {
        this.tabList = tabList;
    }

    private void tabWidthSetting() {
        boolean isWeight = false;
        if (wrapWidth <= width) {
            isWeight = true;
        }
        if (isWeight) {
            for (int i = 0; i < tabsContainer.getChildCount(); i++) {
                tabsContainer.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, 1));
            }
            tabSelectedListener.onAllBtnShow(false);
        } else {
            for (int i = 0; i < tabsContainer.getChildCount(); i++) {
                tabsContainer.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
            tabSelectedListener.onAllBtnShow(true);
        }
    }

    private void addTab(final int position, View tabView, CUSTOM_TAB_DATA data) {
        TextView tvName = (TextView) tabView.findViewById(R.id.tvName);
        ImageView ivNew = (ImageView) tabView.findViewById(R.id.ivNew);
        if (tvName != null) {
            if (data != null) tvName.setText(data.name);
        }

        if (ivNew != null) {
            if (data.isNew) {
                ivNew.setVisibility(View.VISIBLE);
            } else {
                ivNew.setVisibility(View.GONE);
            }
        }

        tabView.setFocusable(true);
        tabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != position) {
                    View tab = tabsContainer.getChildAt(viewPager.getCurrentItem());
                    unSelect(tab);
                    viewPager.setCurrentItem(position);
                    if (tabSelectedListener != null) {
                        tabSelectedListener.onTabSelected(position);
                    }
                }
            }
        });

        tabsContainer.addView(tabView, position, tabLayoutParams);
    }

    private void updateTabStyles() {
        if (tabsContainer == null) {
            return;
        }
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            View v = tabsContainer.getChildAt(i);
            TextView tvName = (TextView) v.findViewById(R.id.tvName);
            if (tvName != null) {
                tvName.setTypeface(tabTextTypeface, tabTextTypefaceStyle);
                if (tvName != null) {
                    if (tabList.size() > i) {
                        if (tabList.get(i).isSelect) {
                            select(tabsContainer.getChildAt(i));
                        } else {
                            unSelect(tabsContainer.getChildAt(i));
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tvName.setAllCaps(true);
                    } else {
                        tvName.setText(tvName.getText().toString().toUpperCase(getResources().getConfiguration().locale));
                    }
                }
            }
        }
    }

    public void scrollToChild(int position, int offset) {
        if (tabList.size() == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
            Pair<Float, Float> lines = getIndicatorCoordinates();
            newScrollX += ((lines.second - lines.first) / 2);
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    private Pair<Float, Float> getIndicatorCoordinates() {
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        if (currentPositionOffset > 0f && currentPosition < tabsContainer.getChildCount() - 1) {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        return new Pair<Float, Float>(lineLeft, lineRight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (tabsContainer.getChildCount() > 0) {
            tabsContainer
                    .getChildAt(0)
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(firstTabGlobalLayoutListener);
        }

        super.onLayout(changed, l, t, r, b);
    }

    private ViewTreeObserver.OnGlobalLayoutListener firstTabGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            View view = tabsContainer.getChildAt(0);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                removeGlobalLayoutListenerPreJB();
            } else {
                removeGlobalLayoutListenerJB();
            }

            if (scrollOffset == 0) scrollOffset = getWidth() / 2;
            currentPosition = viewPager.getCurrentItem();
            currentPositionOffset = 0f;
            scrollToChild(currentPosition, 0);
            updateSelection(currentPosition);
        }

        @SuppressWarnings("deprecation")
        private void removeGlobalLayoutListenerPreJB() {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void removeGlobalLayoutListenerJB() {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabList.size() == 0) {
            return;
        }

        final int height = getHeight();

        if (indicatorHeight > 0) {
            rectPaint.setColor(indicatorColor);
            Pair<Float, Float> lines = getIndicatorCoordinates();
            canvas.drawRect(lines.first, height - indicatorHeight, lines.second, height, rectPaint);
        }
    }

    public void setOnTabSelectedListener(setOnTabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }


    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            int offset = tabsContainer.getChildCount() > 0 ? (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()) : 0;
            scrollToChild(position, offset);
            invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(viewPager.getCurrentItem(), 0);
            }

            View currentTab = tabsContainer.getChildAt(viewPager.getCurrentItem());
            select(currentTab);
            if (viewPager.getCurrentItem() - 1 >= 0) {
                View prevTab = tabsContainer.getChildAt(viewPager.getCurrentItem() - 1);
                unSelect(prevTab);
            }

            if (viewPager.getCurrentItem() + 1 <= viewPager.getAdapter().getCount() - 1) {
                View nextTab = tabsContainer.getChildAt(viewPager.getCurrentItem() + 1);
                unSelect(nextTab);
            }

        }

        @Override
        public void onPageSelected(int position) {
            updateSelection(position);

            View tab = tabsContainer.getChildAt(viewPager.getCurrentItem());
            unSelect(tab);
            viewPager.setCurrentItem(position);
            if (tabSelectedListener != null) {
                tabSelectedListener.onTabSelected(position);
            }

        }

    }

    private void updateSelection(int position) {
        for (int i = 0; i < tabsContainer.getChildCount(); ++i) {
            View tv = tabsContainer.getChildAt(i);
            final boolean selected = i == position;
            if (selected) {
                select(tv);
            } else {
                unSelect(tv);
            }
        }
    }

    private void unSelect(View tab) {
        if (tab == null) {
            return;
        }

        TextView tvName = (TextView) tab.findViewById(R.id.tvName);
        if (tvName == null) {
            return;
        }

        tvName.setTextColor(Color.BLACK);
        tvName.setTypeface(tabTextTypeface, Typeface.NORMAL);

    }

    private void select(View tab) {
        if (tab == null) {
            return;
        }

        TextView tvName = (TextView) tab.findViewById(R.id.tvName);
        if (tvName == null) {
            return;
        }

        tvName.setTextColor(Color.parseColor("#e84418"));
        tvName.setTypeface(tabTextTypeface, Typeface.BOLD);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            tvName.setTextAppearance(R.style.FontMediumBoldTextViewStyle);
//        } else {
//            tvName.setTextAppearance(getContext(), R.style.FontMediumBoldTextViewStyle);
//        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        if (currentPosition != 0 && tabsContainer.getChildCount() > 0) {
            unSelect(tabsContainer.getChildAt(0));
            select(tabsContainer.getChildAt(currentPosition));
        }
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends View.BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View view = (View) getChildAt(getChildCount() - 1);
        int diff = (view.getRight() - (getWidth() + getScrollX()));

        if (diff == 0) {
            tabSelectedListener.onScrollEnd();
        } else {
            tabSelectedListener.onScrolling();
        }
    }

    public interface setOnTabSelectedListener {
        void onTabSelected(int position);

        void onScrollEnd();

        void onScrolling();
        void onAllBtnShow(boolean isShow);
    }
}

