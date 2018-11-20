package com.chacha.kkang.moolbantabs.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.TAB_DATA;

public class ViewSubTab extends LinearLayout {

    private TAB_DATA data;
    private TextView tv;
    private ImageView ivNon;
    private setOnSubTabClickListener listener;
    private String selectColor = "";
    private String noSelectColor = "";

    public ViewSubTab(Context context, setOnSubTabClickListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    public ViewSubTab(Context context, AttributeSet attrs, setOnSubTabClickListener listener) {
        super(context, attrs);
        this.listener = listener;
        init();
    }

    public ViewSubTab(Context context, AttributeSet attrs, int defStyleAttr, setOnSubTabClickListener listener) {
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
                    listener.onSubTabClick(data);
                }
            }
        });
    }

    public void setData(TAB_DATA data) {
        this.data = data;
        updateUI();
    }

    public void setTabResource(int resource) {
        tv.setBackgroundResource(resource);
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
        if (isShowNonImg) {
            ivNon.setVisibility(View.VISIBLE);
        } else {
            ivNon.setVisibility(View.INVISIBLE);
        }
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
            ivNon.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            ivNon.setVisibility(View.GONE);
            tv.setText(data.name);
            if (data.isSelect) {
                tv.setTextColor(Color.parseColor(selectColor));
                tv.setTypeface(null, Typeface.BOLD);
            } else {
                tv.setTextColor(Color.parseColor(noSelectColor));
                tv.setTypeface(null, Typeface.NORMAL);
            }
        }
    }
    public interface setOnSubTabClickListener {
        void onSubTabClick(TAB_DATA data);
    }
}
