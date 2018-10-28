package com.chacha.kkang.moolbantabs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kkang on 2018. 10. 29..
 */

public class PagerView extends LinearLayout {
TextView number;
    public PagerView(Context context) {
        super(context);
    }

    public PagerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
private void init(){
    final LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    li.inflate(R.layout.view, this);

}public PagerView(Context context, int position, TAB_DATA categories) {
        super(context);
        init();
        number = (TextView)findViewById(R.id.number);
        number.setText(categories.key);

    }

}
