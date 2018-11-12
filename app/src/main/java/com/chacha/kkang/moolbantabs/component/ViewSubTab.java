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

    private void updateUI() {
        if (data == null) {
            tv.setVisibility(View.GONE);
            ivNon.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            ivNon.setVisibility(View.GONE);
            tv.setText(data.name);
            if (data.isSelect) {
                tv.setTextColor(Color.parseColor("#3e3e3e"));
                tv.setTypeface(null, Typeface.BOLD);
            } else {
                tv.setTextColor(Color.parseColor("#878787"));
                tv.setTypeface(null, Typeface.NORMAL);
            }
        }
    }
    public interface setOnSubTabClickListener {
        void onSubTabClick(TAB_DATA data);
    }
}
