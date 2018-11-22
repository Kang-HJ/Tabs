package com.chacha.kkang.moolbantabs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemTalkNoti extends LinearLayout {
    TextView tvNotiName;
    TextView tvNotiTitle;
    LinearLayout llMain;


    TALK_NOTI_LIST data;
    setNotiClickLintener lintener;

    public ItemTalkNoti(Context context) {
        super(context);

        init();
    }

    public ItemTalkNoti(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemTalkNoti(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.item_talk_noti, this);

        setUI();
        setEvent();

    }

    private void setUI() {
        tvNotiName = (TextView) findViewById(R.id.tvNotiName);
        tvNotiTitle = (TextView) findViewById(R.id.tvNotiTitle);
        llMain = (LinearLayout) findViewById(R.id.llMain);
    }

    private void setEvent() {
        llMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lintener != null){
                    lintener.onNotiViewClick(data);
                }
            }
        });
    }

    private void updateUI() {
        tvNotiName.setText(data.tc_name);
        tvNotiTitle.setText(data.tl_subject);
    }

    public void setData(TALK_NOTI_LIST data,setNotiClickLintener lintener ) {
        this.lintener = lintener;
        this.data = data;
        updateUI();
    }

    public interface  setNotiClickLintener{
        void onNotiViewClick(TALK_NOTI_LIST data);
    }
}

