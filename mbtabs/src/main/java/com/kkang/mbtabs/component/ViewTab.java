package com.kkang.mbtabs.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkang.mbtabs.R;

public class ViewTab extends LinearLayout {

    private TAB_DATA data;
    private TextView tv;
    private ImageView ivNon;
    private setOnTabClickListener listener;
    private String selectColor = "";
    private String noSelectColor = "";
    private int selectRes = 0;
    private int noSelectRes = 0;
    private String tabType = "Main";
    private boolean isShowNonImg = false;

    public ViewTab(Context context, setOnTabClickListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    public ViewTab(Context context, AttributeSet attrs, setOnTabClickListener listener) {
        super(context, attrs);
        this.listener = listener;
        init();
    }

    public ViewTab(Context context, AttributeSet attrs, int defStyleAttr, setOnTabClickListener listener) {
        super(context, attrs, defStyleAttr);
        this.listener = listener;
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.item_subtabview, this);

        setUI();
        setEvent();
    }

    private void setUI() {
        tv = (TextView) findViewById(R.id.tv);
        ivNon = (ImageView) findViewById(R.id.ivNon);
    }

    private void setEvent() {
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onTabClick(tabType,data);
                }
            }
        });
    }

    public void setData(TAB_DATA data) {
        this.data = data;
        updateUI();
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public void setTabResource(int selectRes, int noSelectRes) {
        this.selectRes = selectRes;
        this.noSelectRes = noSelectRes;
    }

    public void setTabSetting(String selectColor, String noSelectColor, int size) {
        this.selectColor = selectColor;
        this.noSelectColor = noSelectColor;
        tv.setTextSize(size);
    }

    public void setTabMargin(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams viewLayoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
        viewLayoutParams.setMargins(left, top, right, bottom);
        tv.setLayoutParams(viewLayoutParams);
    }

    public void setTabPadding(int left, int top, int right, int bottom) {
        tv.setPadding(left, top, right, bottom);
    }

    public void setNonImgVisible(boolean isShowNonImg) {
        this.isShowNonImg = isShowNonImg;
    }

    public void setNonImgResource(int resource) {
        ivNon.setImageResource(resource);
    }

    public void setNonImgPadding(int left, int top, int right, int bottom) {
        ivNon.setPadding(left, top, right, bottom);
    }

    private void updateUI() {
        if (data == null) {
            tv.setVisibility(View.GONE);
            if (isShowNonImg) {
                ivNon.setVisibility(View.VISIBLE);
            } else {
                ivNon.setVisibility(View.GONE);
            }
        } else {
            if (tabType.equals("Main")) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(data.name);

                if (data.isSelect) {
                    tv.setTextColor(Color.parseColor(selectColor));
                    tv.setBackgroundResource(selectRes);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tv.setTextAppearance(R.style.FontBoldTextViewStyle);
                    } else {
                        tv.setTextAppearance(getContext(), R.style.FontBoldTextViewStyle);
                    }
                } else {
                    tv.setTextColor(Color.parseColor(noSelectColor));
                    tv.setBackgroundResource(noSelectRes);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tv.setTextAppearance(R.style.FontTextViewStyle);
                    } else {
                        tv.setTextAppearance(getContext(), R.style.FontTextViewStyle);
                    }
                }
            } else {
                tv.setVisibility(View.VISIBLE);
                tv.setBackgroundResource(selectRes);
                tv.setText(data.name);
                ivNon.setVisibility(View.GONE);

                if (data.isSelect) {
                    tv.setTextColor(Color.parseColor(selectColor));
                    tv.setTypeface(null, Typeface.BOLD);
                } else {
                    tv.setTextColor(Color.parseColor(noSelectColor));
                    tv.setTypeface(null, Typeface.NORMAL);
                }
            }
        }
    }
    public interface setOnTabClickListener {
        void onTabClick(String type, TAB_DATA data);
    }
}
