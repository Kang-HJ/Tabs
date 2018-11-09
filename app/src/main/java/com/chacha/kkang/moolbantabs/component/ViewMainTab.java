package com.chacha.kkang.moolbantabs.component;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.TAB_DATA;

public class ViewMainTab extends LinearLayout {

    private TAB_DATA data;
    private TextView tv;
    private setOnMainTabClickListener listener;

    public ViewMainTab(Context context, setOnMainTabClickListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    public ViewMainTab(Context context, AttributeSet attrs, setOnMainTabClickListener listener) {
        super(context, attrs);
        this.listener = listener;
        init();
    }

    public ViewMainTab(Context context, AttributeSet attrs, int defStyleAttr, setOnMainTabClickListener listener) {
        super(context, attrs, defStyleAttr);
        this.listener = listener;
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.item_tabview, this);

        setUI();
        setEvent();
    }

    private void setUI() {
        tv = (TextView) findViewById(R.id.tv);
    }

    private void setEvent() {
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMainTabClick(data);
                }
            }
        });
    }

    public void setData(TAB_DATA data) {
        this.data = data;
        updateUI();
    }

    private void updateUI() {
        if (data != null) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(data.name);

            if (data.isSelect) {
                tv.setTextColor(Color.parseColor("#e84418"));
                tv.setBackgroundResource(R.drawable.shape_round_tomato_trans_7);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextAppearance(R.style.FontBoldTextViewStyle);
                } else {
                    tv.setTextAppearance(getContext(), R.style.FontBoldTextViewStyle);
                }
            } else {
                tv.setTextColor(Color.parseColor("#231916"));
                tv.setBackgroundResource(R.drawable.shape_round_line01_white_7);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextAppearance(R.style.FontTextViewStyle);
                } else {
                    tv.setTextAppearance(getContext(), R.style.FontTextViewStyle);
                }
            }
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }

    public interface setOnMainTabClickListener {
        void onMainTabClick(TAB_DATA data);
    }
}
