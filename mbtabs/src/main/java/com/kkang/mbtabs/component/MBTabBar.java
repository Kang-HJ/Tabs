package com.kkang.mbtabs.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Canvas;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkang.mbtabs.R;

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


    public static final int DEF_VALUE_TAB_TEXT_ALPHA = 150;
    private static final int[] ANDROID_ATTRS = new int[]{
            android.R.attr.textColorPrimary,
            android.R.attr.padding,
            android.R.attr.paddingLeft,
            android.R.attr.paddingRight,
    };

    //These indexes must be related with the ATTR array above
    private static final int TEXT_COLOR_PRIMARY = 0;
    private static final int PADDING_INDEX = 1;
    private static final int PADDING_LEFT_INDEX = 2;
    private static final int PADDING_RIGHT_INDEX = 3;

    private LinearLayout mTabsContainer;
    private LinearLayout.LayoutParams mTabLayoutParams;

    private final PagerAdapterObserver mAdapterObserver = new PagerAdapterObserver();
    private final PageListener mPageListener = new PageListener();
    private OnTabSelectedListener mTabReselectedListener = null;
    public ViewPager.OnPageChangeListener mDelegatePageListener;
    public ViewPager mPager;

    private int mTabCount;

    private int mCurrentPosition = 0;
    private float mCurrentPositionOffset = 0f;

    private Paint mRectPaint;
    private Paint mDividerPaint;

    private int mIndicatorColor;
    private int mIndicatorHeight = 2;

    private int mUnderlineHeight = 0;
    private int mUnderlineColor;

    private int mDividerWidth = 0;
    private int mDividerPadding = 0;
    private int mDividerColor;

    private int mTabPadding = 12;
    private int mTabTextSize = 14;
    private ColorStateList mTabTextColor = null;

    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;

    private boolean isExpandTabs = true;
    private boolean isCustomTabs;
    private boolean isPaddingMiddle = false;
    private boolean isTabTextAllCaps = true;

    private Typeface mTabTextTypeface = null;
    private int mTabTextTypefaceStyle = Typeface.BOLD;

    private int mScrollOffset;
    private int mLastScrollX = 0;

    private int mTabBackgroundResId = R.drawable.mb_background_tab;

    public MBTabBar(Context context) {
        this(context, null);
    }

    public MBTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MBTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
        setWillNotDraw(false);
        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(mTabsContainer);

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScrollOffset, dm);
        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight, dm);
        mUnderlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mUnderlineHeight, dm);
        mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding, dm);
        mTabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabPadding, dm);
        mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth, dm);
        mTabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTabTextSize, dm);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(mDividerWidth);

        //Bottom padding for the tabs container parent view to show indicator and underline
        setTabsContainerParentViewPaddings();

        //Configure tab's container LayoutParams for either equal divided space or just wrap tabs
        mTabLayoutParams = isExpandTabs ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        Display dis = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        width = dis.getWidth();
    }

    private void setTabsContainerParentViewPaddings() {
        int bottomMargin = mIndicatorHeight >= mUnderlineHeight ? mIndicatorHeight : mUnderlineHeight;
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), bottomMargin);
    }

    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have tagStrAdapter instance.");
        }

        isCustomTabs = pager.getAdapter() instanceof CustomTabProvider;
        pager.addOnPageChangeListener(mPageListener);
        pager.getAdapter().registerDataSetObserver(mAdapterObserver);
        mAdapterObserver.setAttached(true);
        notifyDataSetChanged();
    }

    int wrapWidth = 0;
    int width = 0;

    public void notifyDataSetChanged() {
        wrapWidth = 0;
        mTabsContainer.removeAllViews();
        mTabCount = mPager.getAdapter().getCount();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            if (isCustomTabs) {
                tabView = ((CustomTabProvider) mPager.getAdapter()).getCustomTabView(this, i);
            } else {
                tabView = LayoutInflater.from(getContext()).inflate(R.layout.item_mb_tab, this, false);
            }

            CharSequence title = mPager.getAdapter().getPageTitle(i);
            addTab(i, title, tabView);
            final int finalI = i;
            mTabsContainer.getChildAt(i).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    /* don't forget to remove the listener after you use it once */
                    if (mTabsContainer.getChildAt(finalI) == null) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mTabsContainer.getChildAt(finalI).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    Debug(" notifyDataSetChanged    layoutContainer   width:" + mTabsContainer.getChildAt(finalI).getWidth());
                    wrapWidth += mTabsContainer.getChildAt(finalI).getWidth();
                    tabWidthSetting();
                    Debug("notifyDataSetChanged    wrapWidth : " + wrapWidth);
                }
            });
        }

        updateTabStyles();
        tabWidthSetting();
    }

    private void tabWidthSetting() {
        boolean isWeight = false;
        if (wrapWidth <= width) {
            isWeight = true;
        }

        if (isWeight) {
            for (int i = 0; i < mTabCount; i++) {
                mTabsContainer.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, 1));
            }
        } else {
            for (int i = 0; i < mTabCount; i++) {
                mTabsContainer.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
    }

    private void addTab(final int position, CharSequence title, View tabView) {
        TextView textView = (TextView) tabView.findViewById(R.id.psts_tab_title);
        if (textView != null) {
            if (title != null) textView.setText(title);
        }

        tabView.setFocusable(true);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() != position) {
                    View tab = mTabsContainer.getChildAt(mPager.getCurrentItem());
                    unSelect(tab);
                    mPager.setCurrentItem(position);
                    if (mTabReselectedListener != null) {
                        mTabReselectedListener.onTabSelected(position);
                    }
                }
            }
        });

        mTabsContainer.addView(tabView, position, mTabLayoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
            v.setBackgroundResource(mTabBackgroundResId);
            v.setPadding(mTabPadding, v.getPaddingTop(), mTabPadding, v.getPaddingBottom());
            TextView tab_title = (TextView) v.findViewById(R.id.psts_tab_title);
            if (tab_title != null) {
                tab_title.setTextColor(mTabTextColor);
                tab_title.setTypeface(mTabTextTypeface, mTabTextTypefaceStyle);
                tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (isTabTextAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab_title.setAllCaps(true);
                    } else {
                        tab_title.setText(tab_title.getText().toString().toUpperCase(getResources().getConfiguration().locale));
                    }
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {
        if (mTabCount == 0) {
            return;
        }

        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            //Half screen offset.
            //- Either tabs start at the middle of the view scrolling straight away
            //- Or tabs start at the begging (no padding) scrolling when indicator gets
            //  to the middle of the view width
            newScrollX -= mScrollOffset;
            Pair<Float, Float> lines = getIndicatorCoordinates();
            newScrollX += ((lines.second - lines.first) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    private Pair<Float, Float> getIndicatorCoordinates() {
        // default: line below current tab
        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (mCurrentPositionOffset > 0f && mCurrentPosition < mTabCount - 1) {
            View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = (mCurrentPositionOffset * nextTabLeft + (1f - mCurrentPositionOffset) * lineLeft);
            lineRight = (mCurrentPositionOffset * nextTabRight + (1f - mCurrentPositionOffset) * lineRight);
        }

        return new Pair<Float, Float>(lineLeft, lineRight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isPaddingMiddle || mPaddingLeft > 0 || mPaddingRight > 0) {
            int width;
            if (isPaddingMiddle) {
                width = getWidth();
            } else {
                // Account for manually set padding for offsetting tab start and end positions.
                width = getWidth() - mPaddingLeft - mPaddingRight;
            }

            //Make sure tabContainer is bigger than the HorizontalScrollView to be able to scroll
            mTabsContainer.setMinimumWidth(width);
            //Clipping padding to false to see the tabs while we pass them swiping
            setClipToPadding(false);
        }

        if (mTabsContainer.getChildCount() > 0) {
            mTabsContainer
                    .getChildAt(0)
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(firstTabGlobalLayoutListener);
        }

        super.onLayout(changed, l, t, r, b);
    }

    private ViewTreeObserver.OnGlobalLayoutListener firstTabGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            View view = mTabsContainer.getChildAt(0);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                removeGlobalLayoutListenerPreJB();
            } else {
                removeGlobalLayoutListenerJB();
            }

            if (isPaddingMiddle) {
                int mHalfWidthFirstTab = view.getWidth() / 2;
                mPaddingLeft = mPaddingRight = getWidth() / 2 - mHalfWidthFirstTab;
            }

            setPadding(mPaddingLeft, getPaddingTop(), mPaddingRight, getPaddingBottom());
            if (mScrollOffset == 0) mScrollOffset = getWidth() / 2 - mPaddingLeft;
            mCurrentPosition = mPager.getCurrentItem();
            mCurrentPositionOffset = 0f;
            scrollToChild(mCurrentPosition, 0);
            updateSelection(mCurrentPosition);
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
        if (isInEditMode() || mTabCount == 0) {
            return;
        }

        final int height = getHeight();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(tab.getRight(), mDividerPadding, tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            canvas.drawRect(mPaddingLeft, height - mUnderlineHeight, mTabsContainer.getWidth() + mPaddingRight, height, mRectPaint);
        }

        // draw indicator line
        if (mIndicatorHeight > 0) {
            mRectPaint.setColor(mIndicatorColor);
            Pair<Float, Float> lines = getIndicatorCoordinates();
            canvas.drawRect(lines.first + mPaddingLeft, height - mIndicatorHeight, lines.second + mPaddingLeft, height, mRectPaint);
        }
    }

    public void setOnTabReselectedListener(OnTabSelectedListener tabReselectedListener) {
        this.mTabReselectedListener = tabReselectedListener;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mDelegatePageListener = listener;
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            int offset = mTabCount > 0 ? (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()) : 0;
            scrollToChild(position, offset);
            invalidate();
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mPager.getCurrentItem(), 0);
            }
            //Full tabTextAlpha for current item
            View currentTab = mTabsContainer.getChildAt(mPager.getCurrentItem());
            select(currentTab);
            //Half transparent for prev item
            if (mPager.getCurrentItem() - 1 >= 0) {
                View prevTab = mTabsContainer.getChildAt(mPager.getCurrentItem() - 1);
                unSelect(prevTab);
            }

            //Half transparent for next item
            if (mPager.getCurrentItem() + 1 <= mPager.getAdapter().getCount() - 1) {
                View nextTab = mTabsContainer.getChildAt(mPager.getCurrentItem() + 1);
                unSelect(nextTab);
            }

            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            updateSelection(position);
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);

            }
            View tab = mTabsContainer.getChildAt(mPager.getCurrentItem());
            unSelect(tab);
            mPager.setCurrentItem(position);
            if (mTabReselectedListener != null) {
                mTabReselectedListener.onTabSelected(position);
            }

        }

    }

    private void updateSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tv = mTabsContainer.getChildAt(i);
            final boolean selected = i == position;
            if (selected) {
                select(tv);
            } else {
                unSelect(tv);
            }
        }
    }

    private void unSelect(View tab) {
        if (tab != null) {
            TextView tab_title = (TextView) tab.findViewById(R.id.psts_tab_title);
            if (tab_title != null) {
                tab_title.setSelected(false);
            }
            if (isCustomTabs) ((CustomTabProvider) mPager.getAdapter()).tabUnselected(tab);
        }
    }

    private void select(View tab) {
        if (tab != null) {
            TextView tab_title = (TextView) tab.findViewById(R.id.psts_tab_title);
            if (tab_title != null) {
                tab_title.setSelected(true);
            }
            if (isCustomTabs) ((CustomTabProvider) mPager.getAdapter()).tabSelected(tab);
        }
    }

    private class PagerAdapterObserver extends DataSetObserver {

        private boolean attached = false;

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        public void setAttached(boolean attached) {
            this.attached = attached;
        }

        public boolean isAttached() {
            return attached;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPager != null) {
            if (!mAdapterObserver.isAttached()) {
                mPager.getAdapter().registerDataSetObserver(mAdapterObserver);
                mAdapterObserver.setAttached(true);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPager != null) {
            if (mAdapterObserver.isAttached()) {
                mPager.getAdapter().unregisterDataSetObserver(mAdapterObserver);
                mAdapterObserver.setAttached(false);
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPosition = savedState.currentPosition;
        if (mCurrentPosition != 0 && mTabsContainer.getChildCount() > 0) {
            unSelect(mTabsContainer.getChildAt(0));
            select(mTabsContainer.getChildAt(mCurrentPosition));
        }
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = mCurrentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
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
        return this.mIndicatorColor;
    }

    public int getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public int getDividerWidth() {
        return mDividerWidth;
    }

    public int getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerPadding() {
        return mDividerPadding;
    }

    public int getScrollOffset() {
        return mScrollOffset;
    }

    public boolean getShouldExpand() {
        return isExpandTabs;
    }

    public int getTextSize() {
        return mTabTextSize;
    }

    public boolean isTextAllCaps() {
        return isTabTextAllCaps;
    }

    public ColorStateList getTextColor() {
        return mTabTextColor;
    }

    public int getTabBackground() {
        return mTabBackgroundResId;
    }

    public int getTabPaddingLeftRight() {
        return mTabPadding;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.mIndicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.mIndicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.mUnderlineColor = getResources().getColor(resId);
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.mDividerColor = getResources().getColor(resId);
        invalidate();
    }

    public void setDividerWidth(int dividerWidthPx) {
        this.mDividerWidth = dividerWidthPx;
        invalidate();
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.mUnderlineHeight = underlineHeightPx;
        invalidate();
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.mDividerPadding = dividerPaddingPx;
        invalidate();
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.mScrollOffset = scrollOffsetPx;
        invalidate();
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.isExpandTabs = shouldExpand;
        if (mPager != null) {
            requestLayout();
        }
    }

    public void setAllCaps(boolean textAllCaps) {
        this.isTabTextAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.mTabTextSize = textSizePx;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        setTextColor(getResources().getColor(resId));
    }

    public void setTextColor(int textColor) {
        setTextColor(createColorStateList(textColor));
    }

    public void setTextColorStateListResource(int resId) {
        setTextColor(getResources().getColorStateList(resId));
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.mTabTextColor = colorStateList;
        updateTabStyles();
    }

    private ColorStateList createColorStateList(int color_state_default) {
        return new ColorStateList(
                new int[][]{
                        new int[]{} //default
                },
                new int[]{
                        color_state_default //default
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

    public void setTypeface(Typeface typeface, int style) {
        this.mTabTextTypeface = typeface;
        this.mTabTextTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.mTabBackgroundResId = resId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.mTabPadding = paddingPx;
        updateTabStyles();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View view = (View) getChildAt(getChildCount() - 1);
        int diff = (view.getRight() - (getWidth() + getScrollX()));

        if (diff == 0){
            mTabReselectedListener.onScrollEnd();
        } else {
            mTabReselectedListener.onScrolling();
        }
    }

    public interface CustomTabProvider {
        View getCustomTabView(ViewGroup parent, int position);

        void tabSelected(View tab);

        void tabUnselected(View tab);
    }

    public interface OnTabSelectedListener {
        void onTabSelected(int position);
        void onScrollEnd();
        void onScrolling();
    }
}

