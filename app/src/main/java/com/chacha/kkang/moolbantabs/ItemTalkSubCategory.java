package com.chacha.kkang.moolbantabs;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkang.mbtabs.component.TAB_DATA;

/**
 * Created by kkang on 2018. 10. 28..
 */

public class ItemTalkSubCategory extends LinearLayout {

    TextView tvCateName;
    TextView tvCateSelected;

    TAB_DATA data;

    LinearLayout llMain;

    public ItemTalkSubCategory(Context context) {
        super(context);
        init();
    }

    public ItemTalkSubCategory(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemTalkSubCategory(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.item_talk_sub_cate, this);

        setUI();
    }

    private void setUI() {
        llMain = (LinearLayout) findViewById(R.id.llMain);

        tvCateName = (TextView) findViewById(R.id.tvCateName);
        tvCateSelected = (TextView) findViewById(R.id.tvCateSelected);
    }

    public void setData(TAB_DATA data) {
        this.data = data;
        updateUI();
    }

    public void updateUI() {
        tvCateName.setText(data.name);
        if (data.isSelect) {
            tvCateSelected.setVisibility(View.VISIBLE);
             tvCateName.setTypeface(null, Typeface.BOLD);
            //tvCateName.setTextAppearance(getContext(), R.style.FontMediumBoldTextViewStyle);
        } else {
            tvCateSelected.setVisibility(View.INVISIBLE);
            tvCateName.setTypeface(null, Typeface.NORMAL);
            //tvCateName.setTextAppearance(getContext(), R.style.FontTextViewStyle);
        }
    }

    public TAB_DATA getData() {
        return data;
    }
}

