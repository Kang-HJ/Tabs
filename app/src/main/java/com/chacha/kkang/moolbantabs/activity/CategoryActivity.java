package com.chacha.kkang.moolbantabs.activity;

import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chacha.kkang.moolbantabs.R;
import com.chacha.kkang.moolbantabs.adapter.Adapter_Pager;
import com.kkang.mbtabs.component.CUSTOM_TAB_DATA;
import com.kkang.mbtabs.component.MBTab;

import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity {

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

    private MBTab mbTab;
    private ArrayList<CUSTOM_TAB_DATA> tabList;

    public ViewPager pager;
    private Adapter_Pager adapterPager;
    private LinearLayout llAll;
    private TextView tvOnResumScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initData();
        setUI();
        setEvent();
        setView();
    }

    private void initData() {
        tabList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            CUSTOM_TAB_DATA data = new CUSTOM_TAB_DATA();
            data.key = i + "";
            data.name = i + " TAB";
            if (i == 3) {
                data.name += "BBBBB";
            }

            if (i == 0) {
                data.isSelect = true;
            }

            data.subList = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                CUSTOM_TAB_DATA sub = new CUSTOM_TAB_DATA();
                sub.key = i + "";
                sub.name = i + " SUB TAB";
                data.isSelect = false;
                if (j == 0) {
                    sub.isSelect = true;
                }
                data.subList.add(sub);
            }
            tabList.add(data);
        }

        adapterPager = new Adapter_Pager(CategoryActivity.this, tabList);
    }

    private void setUI() {
        pager = (ViewPager) findViewById(R.id.pager);
        mbTab = (MBTab) findViewById(R.id.mbTab);
        llAll = (LinearLayout) findViewById(R.id.llAll);
        tvOnResumScroll = (TextView) findViewById(R.id.tvOnResumScroll);
    }

    private void setEvent() {
        tvOnResumScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final double offsetX = 200.0;
                final double duration = 500.0;
                if (mbTab.tabBar != null)
                    mbTab.tabBar.scrollTo((int) offsetX, 0);
                new CountDownTimer((long) duration, 20) {
                    public void onTick(long millisUntilFinished) {
                        int x = (int) (offsetX - (duration - millisUntilFinished) / duration * offsetX);
                        Debug("millisUntilFinished : " + millisUntilFinished + " move x : " + x);
                        if (mbTab.tabBar != null)
                            mbTab.tabBar.smoothScrollTo(x, 0);
                    }

                    public void onFinish() {
                        if (mbTab.tabBar != null)
                            mbTab.tabBar.smoothScrollTo(0, 0);
                    }
                }.start();
            }

        });
    }

    private void setView() {
        llAll.setVisibility(View.GONE);
        pager.setAdapter(adapterPager);
        pager.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);

        mbTab.setData(tabList, llAll);
        mbTab.setViewPager(pager);
    }
}
