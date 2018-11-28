package com.chacha.kkang.moolbantabs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkang.mbtabs.component.CUSTOM_TAB_DATA;
import com.kkang.mbtabs.component.ViewSubTab;

/**
 * Created by kkang on 2018. 10. 29..
 */

public class PagerView extends LinearLayout {
    TextView number;
    ViewSubTab viewSubTab;

    public PagerView(Context context) {
        super(context);
    }

    public PagerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        final LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.view, this);

    }

    public PagerView(Context context, int position, CUSTOM_TAB_DATA categories) {
        super(context);
        init();
        number = (TextView) findViewById(R.id.number);
        number.setText(categories.key);

        viewSubTab = (ViewSubTab)findViewById(R.id.viewSubTab);

        viewSubTab.setData(categories.subList, new ViewSubTab.setSubTabClickListener() {
            @Override
            public void onSubTabclickListener(int position, CUSTOM_TAB_DATA data) {

            }
        });

    }

}
